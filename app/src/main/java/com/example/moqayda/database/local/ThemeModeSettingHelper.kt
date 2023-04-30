package com.example.moqayda.database.local

import android.content.Context
import android.content.SharedPreferences

class ThemeModeSettingHelper {
    companion object {
        const val MODE = "mode"
        var sharedPreferences: SharedPreferences? = null


         fun getSharedPreferencesInstance(context: Context): SharedPreferences {
            if (sharedPreferences == null) {
                sharedPreferences = context.getSharedPreferences(MODE, Context.MODE_PRIVATE)
            }
            return sharedPreferences!!
        }
        fun saveCurrentThemMode(context: Context,nightMode:Boolean){
            val editor = getSharedPreferencesInstance(context).edit()
            editor.putBoolean("night",nightMode)
            editor.apply()
        }
        fun getThemeMode(context: Context): Boolean {
            return getSharedPreferencesInstance(context).getBoolean("night", false)
        }

    }
}