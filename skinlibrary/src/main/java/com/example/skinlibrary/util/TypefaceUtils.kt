package com.example.skinlibrary.util

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import com.example.skinlibrary.config.SkinConfig
import java.io.File

/**
 * Created by dkmFromCG on 2017/9/10.
 * function:字体工具类
 */
class TypefaceUtils{

    companion object {
        lateinit var CURRENT_TYPEFACE: Typeface
        fun createTypeface(context: Context, fontName: String): Typeface {
            val tf: Typeface
            if (!TextUtils.isEmpty(fontName)) {
                tf = Typeface.createFromAsset(context.assets, getFontPath(fontName))
                SkinPreferencesUtils.putString(context, SkinConfig.PREF_CUSTOM_FONT_PATH, getFontPath(fontName))
            } else {
                tf = Typeface.DEFAULT
                SkinPreferencesUtils.putString(context, SkinConfig.PREF_CUSTOM_FONT_PATH, "")
            }
            TypefaceUtils.CURRENT_TYPEFACE = tf
            return tf
        }


        fun getTypeface(context: Context): Typeface {
            val fontPath = SkinPreferencesUtils.getString(context, SkinConfig.PREF_CUSTOM_FONT_PATH)
            val tf: Typeface
            if (!TextUtils.isEmpty(fontPath)) {
                tf = Typeface.createFromAsset(context.assets, fontPath)
            } else {
                tf = Typeface.DEFAULT
                SkinPreferencesUtils.putString(context, SkinConfig.PREF_CUSTOM_FONT_PATH, "")
            }
            return tf
        }

        private fun getFontPath(fontName: String): String {
            return SkinConfig.FONT_DIR_NAME + File.separator + fontName
        }
    }
}