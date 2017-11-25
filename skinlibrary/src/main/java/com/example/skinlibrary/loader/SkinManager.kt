package com.example.skinlibrary.loader

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import com.example.skinlibrary.listener.ISkinLoader
import com.example.skinlibrary.listener.ISkinUpdate
import com.example.skinlibrary.config.SkinConfig
import com.example.skinlibrary.listener.ILoaderListener
import android.content.res.AssetManager
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.support.annotation.RestrictTo
import android.support.v4.content.ContextCompat
import com.example.skinlibrary.base.SkinBaseApplication
import com.example.skinlibrary.util.ResourcesCompat
import com.example.skinlibrary.util.SkinFileUtils
import com.example.skinlibrary.util.TypefaceUtils
import com.thin.downloadmanager.DefaultRetryPolicy
import com.thin.downloadmanager.DownloadRequest
import com.thin.downloadmanager.DownloadStatusListenerV1
import com.thin.downloadmanager.ThinDownloadManager
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread
import java.io.File


/**
 * Created by dkmFromCG on 2017/9/4.
 * function:
 */
class SkinManager private constructor():ISkinLoader{

    companion object {
        /**
         * 单例模式:静态内部类
         * 或者直接把class定义成object,实现双重检验锁的单例功能
         */
        private object Holder{
            @SuppressLint("StaticFieldLeak")
            val instance=SkinManager()
        }
        fun getInstance()=Holder.instance
    }

    private var isDefaultSkin=false
    private var mIsNightMode=false



    private val context:Context = SkinBaseApplication.context
    private lateinit var skinPath:String
    private lateinit var skinPackageName:String
    private var mResources:Resources?=null

    private val skinObservers by lazy {
        ArrayList<ISkinUpdate>()
    }


    fun getCurSkinPackageName()=skinPackageName

    fun getResources() =mResources

    fun getCurSkinPath()=skinPath

    fun isNightMode()=mIsNightMode

    init {
        TypefaceUtils.CURRENT_TYPEFACE = TypefaceUtils.getTypeface(context)
    }

    override fun attach(observer: ISkinUpdate) {
        if (!skinObservers.contains(observer)){
            skinObservers.add(observer)
        }
    }

    override fun detach(observer: ISkinUpdate) {
        if(skinObservers.contains(observer)){
            skinObservers.remove(observer)
        }
    }

    override fun notifySkinUpdate() {
        for (observer in skinObservers){
            observer.onThemeUpdate()
        }
    }

    //load font
    fun loadFont(fontName: String) {
        val tf = TypefaceUtils.createTypeface(context, fontName)
        TextViewRepository.applyFont(tf)
    }

    /**
     * Load resources from apk(local) in async task
     */
    fun loadSkin(skinPackagePath:String=SkinConfig.getCustomSkinPath(context),
             callback: ILoaderListener?){
        if(SkinConfig.isDefaultSkin(context)) return
        async {
            val file=File(skinPackagePath)
            if (!file.exists()){
                return@async
            }

            val packageManager=context.packageManager
            val info=packageManager.getPackageArchiveInfo(skinPackagePath,PackageManager.GET_ACTIVITIES)
            skinPackageName=info.packageName

            val assetManager = AssetManager::class.java.newInstance()
            val addAssetPath = assetManager.javaClass.getMethod("addAssetPath", String::class.java)
            addAssetPath.invoke(assetManager,skinPackagePath)

            val superRes=context.resources
            val skinRes= ResourcesCompat.getResources(assetManager,superRes.displayMetrics,superRes.configuration)

            SkinConfig.saveSkinPath(context,skinPackagePath)

            skinPath=skinPackagePath
            isDefaultSkin=false

            mResources=skinRes

            uiThread {
                if (mResources!=null){
                    callback?.onSuccess()
                    mIsNightMode=false
                    SkinConfig.setNightMode(context,false)
                    notifySkinUpdate()
                }else{
                    isDefaultSkin=true
                    callback?.onFailed("没有获取到资源")
                }
            }

        }

    }

    /**
     * load skin form internet
     * eg:https://raw.githubusercontent.com/burgessjp/ThemeSkinning/master/app/src/main/assets/skin/theme.skin
     */
    fun loadSkinFromUrl(skinUrl:String , callback:ILoaderListener){
        val skinPath = SkinFileUtils.getSkinDir(context)
        val skinName = skinUrl.substring(skinUrl.lastIndexOf("/") + 1)
        val skinFullName = skinPath + File.separator + skinName
        val skinFile = File(skinFullName)
        if (skinFile.exists()) {
            loadSkin(skinName, callback)
            return
        }

        val downloadUri = Uri.parse(skinUrl)
        val destinationUri = Uri.parse(skinFullName)

        val downloadRequest = DownloadRequest(downloadUri)
                .setRetryPolicy(DefaultRetryPolicy())
                .setDestinationURI(destinationUri)
                .setPriority(DownloadRequest.Priority.HIGH)
        callback.onStart()
        downloadRequest.setStatusListener(object :DownloadStatusListenerV1{
            override fun onDownloadComplete(p: DownloadRequest?) {
                loadSkin(skinName, callback)
            }

            override fun onDownloadFailed(p: DownloadRequest?, errorCode: Int, errorMessage: String) {
                callback.onFailed(errorMessage)
            }

            override fun onProgress(p: DownloadRequest?, totalBytes: Long, downloadedBytes: Long, progress: Int) {
                callback.onProgress(progress)
            }
        })

        val manager = ThinDownloadManager()
        manager.add(downloadRequest)
    }

    //夜间模式
    fun nightMode(){
        if (!isDefaultSkin){
            restoreDefaultTheme()
        }
        mIsNightMode=true
        SkinConfig.setNightMode(context,true)
        notifySkinUpdate()
    }

    //恢复到默认主题
    fun restoreDefaultTheme() {
        SkinConfig.saveSkinPath(context, SkinConfig.DEFAULT_SKIN)
        isDefaultSkin = true
        mIsNightMode=false
        SkinConfig.setNightMode(context,false)
        mResources = context.resources
        skinPackageName=context.packageName
        notifySkinUpdate()
    }

    /**
     * whether the skin being used is from net .skin file
     */
    fun isExternalSkin():Boolean{
        return !isDefaultSkin&&mResources!=null
    }

    fun getColorPrimaryDark():Int{
        mResources?.let {
            val identify=mResources!!.getIdentifier("colorPrimaryDark","color",skinPackageName)
            if (identify>0){
                return mResources!!.getColor(identify)
            }
        }
        return -1
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    fun getNightColor(resName:String):Int{
        val resNameNight=resName+"_night"
        val nightResId:Int=mResources!!.getIdentifier(resNameNight,"color",skinPackageName)
        val color:Int
        if (0==nightResId){
            val resId=mResources!!.getIdentifier(resName,"color",skinPackageName)
            color=mResources!!.getColor(resId)
        }else{
            color=mResources!!.getColor(nightResId)
        }
        return color
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    fun getNightDrawable(resName: String): Drawable {

        val resNameNight = resName + "_night"

        var nightResId = mResources!!.getIdentifier(resNameNight, "drawable", skinPackageName)
        if (0==nightResId ) {
            nightResId = mResources!!.getIdentifier(resNameNight, "mipmap", skinPackageName)
        }
        val color: Drawable
        if (0==nightResId) {
            var resId = mResources!!.getIdentifier(resName, "drawable", skinPackageName)
            if (0==resId) {
                resId = mResources!!.getIdentifier(resName, "mipmap", skinPackageName)
            }
            color = mResources!!.getDrawable(resId)
        } else {
            color = mResources!!.getDrawable(nightResId)
        }
        return color
    }

    //region Resource obtain:地区资源获取
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    fun getColor(resId:Int):Int{
        val originColor=ContextCompat.getColor(context,resId)
        if (mResources==null||isDefaultSkin){
            return originColor
        }

        val resName=context.resources.getResourceEntryName(resId)
        val trueResId=mResources!!.getIdentifier(resName,"color",skinPackageName)
        val trueColor:Int

        if (0==trueResId){
            trueColor=originColor
        }else{
            trueColor=mResources!!.getColor(trueResId)
        }
        return trueColor
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    fun getDrawable(resId: Int): Drawable {
        val originDrawable = ContextCompat.getDrawable(context, resId)
        if (mResources == null || isDefaultSkin) {
            return originDrawable
        }
        val resName = context.resources.getResourceEntryName(resId)
        var trueResId = mResources!!.getIdentifier(resName, "drawable", skinPackageName)
        val trueDrawable: Drawable
        if (trueResId == 0) {
            trueResId = mResources!!.getIdentifier(resName, "mipmap", skinPackageName)
        }
        if (trueResId == 0) {
            trueDrawable = originDrawable
        } else {
            if (android.os.Build.VERSION.SDK_INT < 22) {
                trueDrawable = mResources!!.getDrawable(trueResId)
            } else {
                trueDrawable = mResources!!.getDrawable(trueResId, null)
            }
        }
        return trueDrawable
    }

    //get drawable from specific directory
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    fun getDrawable(resId: Int, dir: String): Drawable {
        val originDrawable = ContextCompat.getDrawable(context, resId)
        if (mResources == null || isDefaultSkin) {
            return originDrawable
        }
        val resName = context.resources.getResourceEntryName(resId)
        val trueResId = mResources!!.getIdentifier(resName, dir, skinPackageName)
        val trueDrawable: Drawable
        if (trueResId == 0) {
            trueDrawable = originDrawable
        } else {
            if (android.os.Build.VERSION.SDK_INT < 22) {
                trueDrawable = mResources!!.getDrawable(trueResId)
            } else {
                trueDrawable = mResources!!.getDrawable(trueResId, null)
            }
        }
        return trueDrawable
    }

    /**
     * 加载指定资源颜色drawable,转化为ColorStateList，保证selector类型的Color也能被转换。
     * 无皮肤包资源返回默认主题颜色
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    fun getColorStateList(resId:Int): ColorStateList{

        var isExternalSkin=true
        if (mResources==null||isDefaultSkin){
            isExternalSkin=false
        }

        val resName=context.resources.getResourceEntryName(resId)
        if (isExternalSkin){
            val trueResId=mResources!!.getIdentifier(resName,"color",skinPackageName)

            if (0==trueResId){// 如果皮肤包没有复写该资源，但是需要判断是否是ColorStateList
                return ContextCompat.getColorStateList(context,resId)
            }else{
                return mResources!!.getColorStateList(trueResId)
            }
        }else{
            return ContextCompat.getColorStateList(context,resId)
        }
    }
}