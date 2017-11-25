package com.example.skinlibrary.attr.base

import com.example.skinlibrary.attr.*
import kotlin.properties.Delegates

/**
 * Created by dkmFromCG on 2017/9/4.
 * function: 属性工厂
 */
class AttrFactory {

    companion object {

        fun get(attrName:String, attrValueRefId:Int, attrValueRefName:String, typeName:String): SkinAttr?{
            var skinAttr: SkinAttr by Delegates.notNull()
            if (!isSupportedAttr(attrName)){
                return null
            }
            skinAttr= mSupportedAttr[attrName]!!.clone()

            skinAttr.attrName=attrName
            skinAttr.attrValueRefId=attrValueRefId
            skinAttr.attrValueRefName=attrValueRefName
            skinAttr.attrValueTypeName=typeName
            return skinAttr
        }

        //支持换肤的类型
        var mSupportedAttr= mutableMapOf<String,SkinAttr>(
                Pair("background", BackgroundAttr()),
                Pair("textColor", TextColorAttr()),
                Pair("src", ImageViewAttr())
        )

        /**
         * Check whether the attribute is supported
         * false: not supported
         */
        fun isSupportedAttr(attrName:String):Boolean{
            return mSupportedAttr.containsKey(attrName)
        }

    }

}