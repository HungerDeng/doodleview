package com.example.skinlibrary.entity

import android.view.View
import com.example.skinlibrary.attr.base.SkinAttr

/**
 * Created by MBENBEN on 2017/9/3.
 */
class SkinItem {

    var view:View?=null
    var attrs:MutableList<SkinAttr> = mutableListOf()

    fun apply(){
        if (attrs.isEmpty()){
            return
        }
        for (attr in attrs.listIterator()){
            attr.apply(view!!)
        }
    }

    fun clean(){
        if (attrs.isEmpty()){
            return
        }
        for (attr in attrs.listIterator()){
            //attr=null
        }
    }

    override fun toString(): String {
        return "SkinItem [view=${view!!.javaClass.simpleName} , attrs=$attrs ]"
    }
}