package com.example.skinlibrary.util

import com.example.skinlibrary.loader.SkinManager
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable



/**
 * Created by dkmFromCG on 2017/9/10.
 * function:
 */
class SkinResourcesUtils {

    companion object {
        fun getColor(resId: Int): Int {
            return SkinManager.getInstance().getColor(resId)
        }

        fun getNightColor(resName: String): Int {
            return SkinManager.getInstance().getNightColor(resName)
        }

        fun getDrawable(resId: Int): Drawable {
            return SkinManager.getInstance().getDrawable(resId)
        }

        fun getNightDrawable(resName: String): Drawable {
            return SkinManager.getInstance().getNightDrawable(resName)
        }

        /**
         * get drawable from specific directory

         * @param resId res id
         * *
         * @param dir   res directory
         * *
         * @return drawable
         */
        fun getDrawable(resId: Int, dir: String): Drawable {
            return SkinManager.getInstance().getDrawable(resId, dir)
        }

        fun getColorStateList(resId: Int): ColorStateList {
            return SkinManager.getInstance().getColorStateList(resId)
        }

        fun getColorPrimaryDark(): Int {
            if (!isNightMode()) {
                val resources = SkinManager.getInstance().getResources()
                if (resources != null) {
                    val identify = resources.getIdentifier(
                            "colorPrimaryDark",
                            "color",
                            SkinManager.getInstance().getCurSkinPackageName())
                    if (identify > 0)
                        return resources.getColor(identify)
                }
            } else {
                return SkinManager.getInstance().getNightColor("colorPrimaryDark")
            }
            return -1
        }

        fun isNightMode(): Boolean {
            return SkinManager.getInstance().isNightMode()
        }
    }
}