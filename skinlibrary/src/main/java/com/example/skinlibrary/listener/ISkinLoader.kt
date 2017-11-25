package com.example.skinlibrary.listener

/**
 * Created by dkmFromCG on 2017/9/4.
 * function: 观察者模式
 */
interface ISkinLoader {
    fun attach(observer: ISkinUpdate)
    fun detach(observer: ISkinUpdate)
    fun notifySkinUpdate()
}