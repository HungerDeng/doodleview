package com.example.skinlibrary.util

import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics

/**
 * Created by dkmFromCG on 2017/9/10.
 * function:
 */
class ResourcesCompat {

    companion object {

        fun getResources(assetManager: AssetManager,
                         displayMetrics: DisplayMetrics,
                         configuration: Configuration): Resources ?{
            var resources: Resources? = null
            try {
                resources = Resources(assetManager, displayMetrics, configuration)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return resources
        }
    }
}