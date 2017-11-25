package com.example.skinlibrary.util


import android.content.Context
import com.example.skinlibrary.globalfun.preferencesPut


/**
 * Created by dkmFromCG on 2017/9/4.
 * function:
 */
class SkinPreferencesUtils {
    companion object {
        val PREFERENCE_NAME="dkm_skin_library_pref"

        /**
         * put string preferences
         * @param context
         * @param key The name of the preference to modify
         * @param value The new value for the preference
         * @return True if the new values were successfully written to persistent storage.
         */
        fun putString(context: Context, key: String, value: String): Boolean {
            val settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            val editor = settings.edit()
            editor.putString(key, value)
            return editor.commit()
        }


        /**
         * get string preferences
         * @param context
         * @param key The name of the preference to retrieve
         * @param defaultValue Value to return if this preference does not exist
         * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
         *         this name that is not a string
         */
        fun getString(context: Context, key: String, defaultValue: String?=null): String {
            val settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            return settings.getString(key, defaultValue)
        }

        /**
         * put int preferences
         * @param context
         * @param key The name of the preference to modify
         * @param value The new value for the preference
         * @return True if the new values were successfully written to persistent storage.
         */
        fun putInt(context: Context, key: String, value: Int): Boolean {
            val settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            val editor = settings.edit()
            editor.putInt(key, value)
            return editor.commit()
        }

        /**
         * get int preferences
         * @param context
         * @param key The name of the preference to retrieve
         * @param defaultValue Value to return if this preference does not exist
         * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
         *         this name that is not a int
         */
        fun getInt(context: Context, key: String, defaultValue: Int=-1): Int {
            val settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            return settings.getInt(key, defaultValue)
        }

        /**
         * put long preferences
         * @param context
         * @param key The name of the preference to modify
         * @param value The new value for the preference
         * @return True if the new values were successfully written to persistent storage.
         */
        fun putLong(context: Context, key: String, value: Long) :Boolean{
            return preferencesPut(context){
                editor ->
                editor.putLong(key,value)
            }
        }

        /**
         * get long preferences
         * @param context
         * @param key The name of the preference to retrieve
         * @param defaultValue Value to return if this preference does not exist
         * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
         *         this name that is not a long
         */
        fun getLong(context: Context, key: String, defaultValue: Long=-1L): Long {
            val settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            return settings.getLong(key, defaultValue)
        }

        /**
         * put float preferences
         * @param context
         * @param key The name of the preference to modify
         * @param value The new value for the preference
         * @return True if the new values were successfully written to persistent storage.
         */
        fun putFloat(context: Context, key: String, value: Float): Boolean {
            val settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            val editor = settings.edit()
            editor.putFloat(key, value)
            return editor.commit()
        }

        /**
         * get float preferences
         * @param context
         * @param key The name of the preference to retrieve
         * @param defaultValue Value to return if this preference does not exist
         * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
         *         this name that is not a float
         */
        fun getFloat(context: Context, key: String, defaultValue: Float=-1f): Float {
            val settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            return settings.getFloat(key, defaultValue)
        }

        /**
         * put boolean preferences
         * @param context
         * @param key The name of the preference to modify
         * @param value The new value for the preference
         * @return True if the new values were successfully written to persistent storage.
         */
        fun putBoolean(context: Context, key: String, value: Boolean): Boolean {
            val settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            val editor = settings.edit()
            editor.putBoolean(key, value)
            return editor.commit()
        }

        /**
         * get boolean preferences
         * @param context
         * @param key The name of the preference to retrieve
         * @param defaultValue Value to return if this preference does not exist
         * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
         *         this name that is not a boolean
         */
        fun getBoolean(context: Context, key: String, defaultValue: Boolean=false): Boolean {
            val settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            return settings.getBoolean(key, defaultValue)
        }
    }




}