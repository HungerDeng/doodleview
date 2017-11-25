package com.example.skinlibrary.util

import android.app.Application
import android.content.Context
import android.os.Environment
import java.nio.file.Files.exists
import android.os.Environment.MEDIA_MOUNTED
import com.example.skinlibrary.config.SkinConfig
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.IOException


/**
 * Created by dkmFromCG on 2017/9/4.
 * function:
 */
class SkinFileUtils {


    companion object {
        /**
         * 复制assets/skin目录下的皮肤文件到指定目录
         * @param context the context
         * @param name    皮肤名
         * @param toDir   指定目录
         */
        fun copySkinAssetsToDir(context: Context, name: String, toDir: String) {
            val toFile = toDir + File.separator + name
            try {
                val inputStream = context.assets.open(SkinConfig.SKIN_DIR_NAME + File.separator + name)
                val fileDir = File(toDir)
                if (!fileDir.exists()) {
                    fileDir.mkdirs()
                }
                val os = FileOutputStream(toFile)
                val bytes = ByteArray(1024)
                val byteCount: Int=inputStream.read(bytes)
                while (byteCount!= -1) {
                    os.write(bytes, 0, byteCount)
                }
                os.close()
                inputStream.close()

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        /**
         * 得到存放皮肤的目录
         * @param context the context
         * @return 存放皮肤的目录
         */
        fun getSkinDir(context: Context): String {
            val skinDir = File(getCacheDir(context), SkinConfig.SKIN_DIR_NAME)
            if (!skinDir.exists()) {
                skinDir.mkdirs()
            }
            return skinDir.absolutePath
        }

        /**
         * 得到手机的缓存目录
         */
        fun getCacheDir(context: Context): String {
            if (Environment.getExternalStorageState()==Environment.MEDIA_MOUNTED) {
                val cacheDir = context.externalCacheDir
                if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
                    return cacheDir.absolutePath
                }
            }

            val cacheDir = context.cacheDir

            return cacheDir.absolutePath
        }
    }
}