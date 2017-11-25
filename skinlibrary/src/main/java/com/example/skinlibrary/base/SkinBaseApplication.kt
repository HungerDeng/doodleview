package com.example.skinlibrary.base


import android.app.Application
import android.content.Context
import com.example.skinlibrary.config.SkinConfig
import com.example.skinlibrary.loader.SkinManager
import com.example.skinlibrary.util.SkinFileUtils
import java.io.File
import kotlin.properties.Delegates

/**
 * Created by dkmFromCG on 2017/9/4.
 * function:
 */
open class SkinBaseApplication :Application() {

    companion object {
        var context by Delegates.notNull<Context>()
    }


    override fun onCreate() {
        super.onCreate()
        context=this
    }

    init {
        setUpSkinFile()
        val sm=SkinManager.getInstance()
        if (!SkinConfig.isNightMode(this)){
            sm.loadSkin(callback=null)
        }else{
            sm.nightMode()
        }
    }

    private fun setUpSkinFile() {
        val skinFiles=assets.list(SkinConfig.SKIN_DIR_NAME)
        for(skinFileName in skinFiles.iterator()){
            val file=File(SkinFileUtils.getSkinDir(this),skinFileName)
            if (!file.exists()){
                SkinFileUtils.copySkinAssetsToDir(this,skinFileName,SkinFileUtils.getSkinDir(this))
            }
        }
    }

}