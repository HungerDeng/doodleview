package com.example.skinlibrary.globalfun

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.preference.Preference
import com.example.skinlibrary.util.SkinPreferencesUtils


/**
 * Created by dkmFromCG on 2017/9/10.
 * function:
 */

inline fun supportsLollipop(code: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        code()
    }
}




inline fun preferencesPut(context: Context, putData:(editor: SharedPreferences.Editor) -> Unit):Boolean{
    val settings = context.getSharedPreferences(SkinPreferencesUtils.PREFERENCE_NAME, Context.MODE_PRIVATE)
    val editor = settings.edit()
    putData(editor)
    return editor.commit()
}

fun Preference.get(){

}
