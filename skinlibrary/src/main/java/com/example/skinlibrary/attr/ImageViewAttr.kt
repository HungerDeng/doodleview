package com.example.skinlibrary.attr

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import com.example.skinlibrary.attr.base.SkinAttr
import com.example.skinlibrary.util.SkinResourcesUtils

/**
 * Created by dkmFromCG on 2017/9/10.
 * function:
 */
class ImageViewAttr : SkinAttr() {
    override fun applySkin(view: View) {
        if (view is ImageView){
            val imageView=view
            if (isDrawable()){
                imageView.setImageDrawable(SkinResourcesUtils.getDrawable(attrValueRefId))
            }else if (isColor()){
                imageView.setImageDrawable(ColorDrawable(SkinResourcesUtils.getColor(attrValueRefId)))
            }
        }
    }

    override fun applyNightMode(view: View) {

    }

}