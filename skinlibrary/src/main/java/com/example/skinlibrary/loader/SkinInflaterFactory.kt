package com.example.skinlibrary.loader

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.support.v4.view.LayoutInflaterFactory
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.example.skinlibrary.attr.base.AttrFactory
import com.example.skinlibrary.attr.base.DynamicAttr
import com.example.skinlibrary.attr.base.SkinAttr
import com.example.skinlibrary.config.SkinConfig
import com.example.skinlibrary.entity.SkinItem
import com.example.skinlibrary.util.SkinL
import com.example.skinlibrary.util.SkinListUtils
import kotlin.properties.Delegates


/**
 * Created by dkmFromCG on 2017/9/4.
 * function:自定义的InflaterFactory，用来代替默认的LayoutInflaterFactory
 */
class SkinInflaterFactory : LayoutInflaterFactory {
    companion object {
        val TAG="SkinInflaterFactory"
    }
    /**
     * 存储那些有皮肤更改需求的View及其对应的属性的集合
     */
    val mSkinItemMap= mutableMapOf<View,SkinItem>()

    var mAppCompatActivity by Delegates.notNull<AppCompatActivity>()

    override fun onCreateView(parent: View?, name: String?, context: Context, attrs: AttributeSet): View? {
        val isSkinEnable:Boolean=attrs.getAttributeBooleanValue(SkinConfig.NAMESPACE, SkinConfig.ATTR_SKIN_ENABLE, false)
        val delegate: AppCompatDelegate=mAppCompatActivity.delegate
        var view=delegate.createView(parent, name, context, attrs)
        if (view is TextView &&SkinConfig.isCanChangeFont){
            TextViewRepository.add(mAppCompatActivity,view)
        }

        if (isSkinEnable || SkinConfig.isGlobalSkinApply) {
            if (view == null) {
                view = ViewProducer.createViewFromTag(context, name, attrs)
            }
            if (null==view){
                return null
            }
            parseSkinAttr(context, attrs, view)
        }
        return view

    }


    /**
     * Collect skin able tag such as background , textColor and so on
     */
    private fun  parseSkinAttr(context: Context, attrs: AttributeSet, view: View) {
        val viewAttrs = ArrayList<SkinAttr>()//存储View可更换皮肤属性的集合
        SkinL.i(TAG, "viewName:" + view.javaClass.simpleName)
        for (i in 0..attrs.attributeCount){
            val attrName=attrs.getAttributeName(i)//属性名
            val attrValue=attrs.getAttributeValue(i)//属性值
            SkinL.i(TAG, "AttributeName:$attrName|attrValue:$attrValue")
            //region style
            if ("style"==attrName){//style name
                val styleName:String=attrValue.substring(attrValue.indexOf("/")+1)
                val styleId=context.resources.getIdentifier(styleName,"style",context.packageName)
                //textColor在R文件中
                val skinAttrs = intArrayOf(textColor, android.R.attr.background)
                val typedArray: TypedArray=context.theme.obtainStyledAttributes(styleId,skinAttrs)
                val textColorId:Int=typedArray.getResourceId(0,-1)
                ??
                val backgroundId:Int=typedArray.getResourceId( 1,-1)
                if (-1!=textColorId){
                    //入口名，例如text_color_selector
                    val entryName :String= context.resources.getResourceEntryName(textColorId)
                    val typeName :String= context.resources.getResourceTypeName(textColorId)
                    val skinAttr :SkinAttr?= AttrFactory.get("textColor", textColorId, entryName, typeName)
                    SkinL.w(TAG, "    textColor in style is supported:\n" +
                            "    resource id:$textColorId\n" +
                            "    attrName:$attrName\n" +
                            "    attrValue:$attrValue\n" +
                            "    entryName:$entryName\n" +
                            "    typeName:$typeName")
                    skinAttr?.let {
                        viewAttrs.add(skinAttr)
                    }
                }
                if (-1!=backgroundId){
                    //入口名，例如text_color_selector
                    val entryName :String= context.resources.getResourceEntryName(backgroundId)
                    val typeName :String= context.resources.getResourceTypeName(backgroundId)
                    val skinAttr :SkinAttr?= AttrFactory.get("background", backgroundId, entryName, typeName)
                    SkinL.w(TAG, "    background in style is supported:\n" +
                            "    resource id:$backgroundId\n" +
                            "    attrName:$attrName\n" +
                            "    attrValue:$attrValue\n" +
                            "    entryName:$entryName\n" +
                            "    typeName:$typeName")
                    skinAttr?.let {
                        viewAttrs.add(skinAttr)
                    }
                }

                typedArray.recycle()
                continue
            }
            //endregion

            if (AttrFactory.isSupportedAttr(attrName) &&attrValue.startsWith("@")){
                val id:Int=attrValue.substring(1).toInt()//资源的ID
                if (0==id) continue
                //入口名，例如text_color_selector
                val entryName=context.resources.getResourceEntryName(id)
                //类型名，例如color、drawable
                val typeName=context.resources.getResourceTypeName(id)
                val skinAttr= AttrFactory.get(attrName,id,entryName,typeName)
                SkinL.w(TAG, "    $attrName is supported:\n" +
                        "    resource id:$id\n" +
                        "    attrName:$attrName\n" +
                        "    attrValue:$attrValue\n" +
                        "    entryName:$entryName\n" +
                        "    typeName:$typeName")
                skinAttr?.let {
                    viewAttrs.add(skinAttr)
                }
            }
        }

        if (!SkinListUtils.isEmpty(viewAttrs)) {
            val skinItem = SkinItem()
            skinItem.view = view
            skinItem.attrs = viewAttrs
            mSkinItemMap.put(skinItem.view!!, skinItem)
            if (SkinManager.getInstance().isExternalSkin() || SkinManager.getInstance().isNightMode()) {//如果当前皮肤来自于外部或者是处于夜间模式
                skinItem.apply()
            }
        }
    }

    fun applySkin(){
        if (mSkinItemMap.isEmpty()){
            return
        }
        mSkinItemMap.keys
                .forEach { mSkinItemMap[it]!!.apply() }
    }

    //清除有皮肤更改需求的View及其对应的属性的集合
    fun clean(){
        mSkinItemMap.keys
                .forEach { mSkinItemMap[it]!!.clean() }

        TextViewRepository.remove(mAppCompatActivity)
        mSkinItemMap.clear()
    }

    private fun  addSkinView(skinItem: SkinItem) {
        mSkinItemMap[skinItem.view]?.let {
            mSkinItemMap[skinItem.view]!!.attrs.addAll(skinItem.attrs)
        }?:(mSkinItemMap.put(skinItem.view!!,skinItem))
    }

    fun removeSkinView(view: View){
        val skinItem=mSkinItemMap.remove(view)
        skinItem?.let {
            SkinL.w(TAG,"removeSkinView from mSkinItemMap:" + skinItem.view)
        }
        if (SkinConfig.isCanChangeFont&&view is TextView){
            SkinL.e(TAG,"removeSkinView from TextViewRepository:" + view)
            TextViewRepository.remove(mAppCompatActivity,view)
        }
    }

    /**
     * 动态添加那些有皮肤更改需求的View，及其对应的属性
     */
    fun dynamicAddSkinEnableView(context: Context,view: View,pDAttrs:List<DynamicAttr>){
        val viewAttrs=ArrayList<SkinAttr>()
        val skinItem=SkinItem()
        skinItem.view=view

        for (dAttr in pDAttrs.listIterator()){
            val id=dAttr.refResId
            val entryName=context.resources.getResourceEntryName(id)
            val typeName=context.resources.getResourceTypeName(id)
            val skinAttr= AttrFactory.get(dAttr.attrName,id,entryName,typeName)
            viewAttrs.add(skinAttr!!)
        }

        skinItem.attrs=viewAttrs
        skinItem.apply()
        addSkinView(skinItem)
    }

    /**
     * 动态添加那些有皮肤更改需求的View，及其对应的属性集合
     */
    fun dynamicAddSkinEnableView(context: Context,view: View,attrName:String,attrValueResId:Int){
        val entryName = context.resources.getResourceEntryName(attrValueResId)
        val typeName = context.resources.getResourceTypeName(attrValueResId)
        val mSkinAttr = AttrFactory.get(attrName, attrValueResId, entryName, typeName)
        val skinItem = SkinItem()
        skinItem.view = view
        val viewAttrs = ArrayList<SkinAttr>()
        viewAttrs.add(mSkinAttr!!)
        skinItem.attrs = viewAttrs
        skinItem.apply()
        addSkinView(skinItem)
    }

    //动态添加字体
    fun dynamicAddFontEnableView(activity:Activity,textView: TextView){
        TextViewRepository.add(activity,textView)
    }

}