package com.example.skinlibrary.attr

import android.view.View
import android.widget.TextView
import com.example.skinlibrary.attr.base.SkinAttr
import com.example.skinlibrary.util.SkinResourcesUtils

/**
 * Created by dkmFromCG on 2017/9/10.
 * function:
 */
class TextColorAttr : SkinAttr() {
    override fun applySkin(view: View) {
        if (view is TextView){
            val textView =view
            if (isColor()){
                textView.setTextColor(SkinResourcesUtils.getColorStateList(attrValueRefId))
            }
        }
    }

    override fun applyNightMode(view: View) {
        if (view is TextView){
            val textView =view
            if (isColor()){
                textView.setTextColor(SkinResourcesUtils.getNightColor(attrValueRefName))
            }
        }
    }

}