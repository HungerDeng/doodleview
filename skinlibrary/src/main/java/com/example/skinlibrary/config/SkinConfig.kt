package com.example.skinlibrary.config

import android.content.Context
import com.example.skinlibrary.attr.base.AttrFactory
import com.example.skinlibrary.attr.base.SkinAttr
import com.example.skinlibrary.util.SkinPreferencesUtils



/**
 * Created by dkmFromCG on 2017/9/4.
 * function:皮肤配置
 */
class SkinConfig {
    companion object {
        val NAMESPACE="http://schemas.android.com/android/skin"
        //preferences_custom:本地数据存储的skinPath
        val PREF_CUSTOM_SKIN_PATH="dkm_skin_custom_path"
        val PREF_CUSTOM_FONT_PATH="dkm_skin_font_path"
        //属性中的"skinEnable"
        val ATTR_SKIN_ENABLE="enable"
        val DEFAULT_SKIN="dkm_skin_default"
        val PREF_NIGHT_MODE="night_mode"

        val SKIN_DIR_NAME = "skin"
        val FONT_DIR_NAME = "fonts"

        var isCanChangeStatusColor = false
        var isCanChangeFont = false
        //apply skin for global and you don't to set  skin:enable="true"  in layout
        var isGlobalSkinApply = false


        /**
         * get path of last skin package path
         */
        fun getCustomSkinPath(context: Context): String {
            return SkinPreferencesUtils.getString(context, PREF_CUSTOM_SKIN_PATH, DEFAULT_SKIN)
        }

        fun saveSkinPath(context: Context, path: String) {
            SkinPreferencesUtils.putString(context, PREF_CUSTOM_SKIN_PATH, path)
        }

        fun saveFontPath(context: Context, path: String) {
            SkinPreferencesUtils.putString(context, PREF_CUSTOM_FONT_PATH, path)
        }

        fun isDefaultSkin(context: Context): Boolean {
            return DEFAULT_SKIN==getCustomSkinPath(context)
        }

        fun setNightMode(context: Context,isEnableNightMode:Boolean){
            SkinPreferencesUtils.putBoolean(context, PREF_NIGHT_MODE, isEnableNightMode)
        }

        fun isNightMode(context: Context):Boolean{
            return SkinPreferencesUtils.getBoolean(context, PREF_NIGHT_MODE, false)
        }

        /**
         * add custom skin attribute support
         */
        fun addSupportAttr(attrName:String,  skinAttr: SkinAttr){
            AttrFactory.mSupportedAttr.put(attrName,skinAttr)
        }

    }
}