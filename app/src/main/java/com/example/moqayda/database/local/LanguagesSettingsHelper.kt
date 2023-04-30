package com.example.moqayda.database.local

import android.content.Context
import android.content.SharedPreferences

class LanguagesSettingsHelper {
    companion object{
        var sharedPreferences:SharedPreferences?=null


        fun getSharedPreferencesInstance(context: Context):SharedPreferences{
            if(sharedPreferences ==null){
                sharedPreferences =  context.getSharedPreferences("lang", Context.MODE_PRIVATE)
            }
            return sharedPreferences!!
        }

        fun putData(lang:String,context: Context){

            var editor = getSharedPreferencesInstance(context).edit()
            editor.putString("lang",lang)

            editor.apply()
        }

        fun retreiveDataFromSharedPreferences(key: String, context: Context): String{
            var data = getSharedPreferencesInstance(context).getString(key,"")
            return data!!
        }

    }

}