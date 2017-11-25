package com.example.skinlibrary.attr

import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import com.example.skinlibrary.attr.base.SkinAttr
import com.example.skinlibrary.loader.SkinManager
import com.example.skinlibrary.util.SkinL
import com.example.skinlibrary.util.SkinResourcesUtils

/**
 * Created by dkmFromCG on 2017/9/10.
 * function:
 */
class BackgroundAttr : SkinAttr() {


    override fun applySkin(view: View) {
        if (isColor()){
            val color = SkinResourcesUtils.getColor(attrValueRefId)
            view.setBackgroundColor(color)
        }else{
            val bg = SkinResourcesUtils.getDrawable(attrValueRefId)
            view.setBackgroundDrawable(bg)
        }
    }

    override fun applyNightMode(view: View) {
        if (isColor()){
            view.setBackgroundColor(SkinResourcesUtils.getNightColor(attrValueRefName))
        }else{
            view.setBackgroundDrawable(SkinResourcesUtils.getNightDrawable(attrValueRefName))
        }
    }

}