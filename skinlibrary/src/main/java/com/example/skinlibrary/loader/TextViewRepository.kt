package com.example.skinlibrary.loader

import android.app.Activity
import android.graphics.Typeface
import android.widget.TextView
import com.example.skinlibrary.util.TypefaceUtils
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by dkmFromCG on 2017/9/10.
 * function:
 */
class TextViewRepository {

    companion object {
        private val mTextViewMap = mutableMapOf<Activity, MutableList<TextView>>()

        fun add(activity: Activity, textView: TextView) {

            if (mTextViewMap.containsKey(activity)) {
                mTextViewMap[activity]!!.add(textView)
            } else {
                val textViews = ArrayList<TextView>()
                textViews.add(textView)
                mTextViewMap.put(activity, textViews)
            }
            textView.typeface = TypefaceUtils.CURRENT_TYPEFACE
        }

        fun remove(activity: Activity) {
            mTextViewMap.remove(activity)
        }

        fun remove(activity: Activity, textView: TextView): Boolean {
            return mTextViewMap.containsKey(activity) && mTextViewMap[activity]!!.remove(textView)
        }

        fun applyFont(tf: Typeface) {
            mTextViewMap.keys
                    .flatMap { mTextViewMap[it]!! }
                    .forEach { it.typeface=tf }
        }

        /*static void applyFont(Typeface tf) {
            for (Activity activity : mTextViewMap.keySet()) {
                for (TextView textView : mTextViewMap.get(activity)) {
                textView.setTypeface(tf);
            }
            }
        }*/
    }
}