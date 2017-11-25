package com.example.skinlibrary.attr.base

import android.view.View
import com.example.skinlibrary.util.SkinResourcesUtils

/**
 * Created by MBENBEN on 2017/9/2.
 */
abstract class SkinAttr :Cloneable{

    companion object {
        val RES_TYPE_NAME_COLOR="color"
        val RES_TYPE_NAME_DRAWABLE="drawable"
        val RES_TYPE_NAME_MIPMAP = "mipmap"
    }

    /**
     * name of the attr, ex: background or textSize or textColor
     */
    lateinit var attrName:String

    /**
     * id of the attr value refered to, normally is [2130745655]
     */
    var attrValueRefId:Int=2130745655

    /**
     * entry name of the value , such as [app_exit_btn_background]
     */
    lateinit var attrValueRefName:String

    /**
     * type of the value , such as color or drawable
     */
    lateinit var attrValueTypeName:String

    public override fun clone(): SkinAttr {
        return super.clone() as SkinAttr
    }

    //Use to apply view with new TypedValue
    fun apply(view: View){
        if (!SkinResourcesUtils.isNightMode()){
            applySkin(view)
        }else{
            applyNightMode(view)
        }
    }

    abstract fun  applySkin(view: View)

    abstract fun  applyNightMode(view: View)

    fun isDrawable()=(RES_TYPE_NAME_DRAWABLE == attrValueTypeName || RES_TYPE_NAME_MIPMAP == attrValueTypeName)

    fun isColor()= (RES_TYPE_NAME_COLOR == attrValueTypeName)

    override fun toString(): String {
        return "SkinAttr \n" +
                "[\n" +
                "attrName=$attrName, \n"+
                "attrValueRefId=$attrValueRefId, \n"+
                "attrValueRefName=$attrValueRefName , \n"+
                "attrValueTypeName=$attrValueTypeName\n" +
                "]"
    }
}