package com.example.skinlibrary.listener

import android.view.View
import android.widget.TextView
import com.example.skinlibrary.attr.base.DynamicAttr

/**
 * Created by dkmFromCG on 2017/9/10.
 * function:
 */

interface IDynamicNewView {
    fun dynamicAddView(view:View,pDAttrs:MutableList<DynamicAttr>)
    fun dynamicAddView(view: View,attrName:String,attrValueResId:Int)

    /**
     * add the textView for font switch
     */
    fun dynamicAddFontView(textView: TextView)

}