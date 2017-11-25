package com.example.skinlibrary.listener

/**
 * Created by MBENBEN on 2017/9/2.
 */
interface ILoaderListener {
    fun onStart()
    fun onSuccess()
    fun onFailed( errMsg:String)

    /**
     * called when from network load skin

     * @param progress download progress
     */
    abstract fun onProgress(progress: Int)
}