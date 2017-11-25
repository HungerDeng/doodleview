package com.example.skinlibrary.base

import android.os.Build
import android.os.Bundle
import android.support.v4.view.LayoutInflaterCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.example.skinlibrary.attr.base.DynamicAttr
import com.example.skinlibrary.config.SkinConfig
import com.example.skinlibrary.globalfun.supportsLollipop
import com.example.skinlibrary.listener.IDynamicNewView
import com.example.skinlibrary.listener.ISkinUpdate
import com.example.skinlibrary.loader.SkinInflaterFactory
import com.example.skinlibrary.loader.SkinManager
import com.example.skinlibrary.util.SkinL
import com.example.skinlibrary.util.SkinResourcesUtils

/**
 * Created by dkmFromCG on 2017/9/4.
 * function:
 */
open class SkinBaseActivity : AppCompatActivity() ,ISkinUpdate ,IDynamicNewView{

    companion object {
        val TAG="SkinBaseActivity"
    }
    private val mSkinInflaterFactory: SkinInflaterFactory by lazy {
        SkinInflaterFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mSkinInflaterFactory
        LayoutInflaterCompat.setFactory(layoutInflater,mSkinInflaterFactory)
        super.onCreate(savedInstanceState)
        changeStatusColor()
    }

    override fun onResume() {
        super.onResume()
        SkinManager.getInstance().attach(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        SkinManager.getInstance().detach(this)
        mSkinInflaterFactory.clean()
    }

    override fun onThemeUpdate() {
        SkinL.i(TAG, "onThemeUpdate")
        mSkinInflaterFactory.applySkin()
        changeStatusColor()
    }

    override fun dynamicAddView(view: View, pDAttrs: MutableList<DynamicAttr>) {
        mSkinInflaterFactory.dynamicAddSkinEnableView(this,view,pDAttrs)
    }

    override fun dynamicAddView(view: View, attrName: String, attrValueResId: Int) {
        mSkinInflaterFactory.dynamicAddSkinEnableView(this,view,attrName,attrValueResId)
    }

    override fun dynamicAddFontView(textView: TextView) {
        mSkinInflaterFactory.dynamicAddFontEnableView(this,textView)
    }

    fun getSkinInflaterFactory()=mSkinInflaterFactory

    fun changeStatusColor(){
        if (!SkinConfig.isCanChangeStatusColor){
            return
        }
        supportsLollipop {
            val color = SkinResourcesUtils.getColorPrimaryDark()
            if (color != -1) {
                val window = window
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = SkinResourcesUtils.getColorPrimaryDark()
                }
            }
        }
    }


}