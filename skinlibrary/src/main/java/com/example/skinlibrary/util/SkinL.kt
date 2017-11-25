package com.example.skinlibrary.util

import android.util.Log


/**
 * Created by MBENBEN on 2017/9/2.
 */
class SkinL {

    companion object {
        private val DEBUG=true
        private val TAG = "SkinLoader"

        fun i(msg: String) {
            if (DEBUG) {
                Log.i(TAG, msg)
            }
        }

        fun d(msg: String) {
            if (DEBUG) {
                Log.d(TAG, msg)
            }
        }

        fun w(msg: String) {
            if (DEBUG) {
                Log.w(TAG, msg)
            }
        }

        fun e(msg: String) {
            if (DEBUG) {
                Log.e(TAG, msg)
            }
        }

        fun i(tag: String, msg: String) {
            if (DEBUG) {
                Log.i(tag, msg)
            }
        }

        fun d(tag: String, msg: String) {
            if (DEBUG) {
                Log.d(tag, msg)
            }
        }

        fun w(tag: String, msg: String) {
            if (DEBUG) {
                Log.w(tag, msg)
            }
        }

        fun e(tag: String, msg: String) {
            if (DEBUG) {
                Log.e(tag, msg)
            }
        }
    }

}