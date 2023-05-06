package com.example.moqayda

import androidx.room.TypeConverter
import com.example.moqayda.models.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

object ProductTypeConvert{
@TypeConverter
open fun storedStringToMyObjects(data: String?): List<Product?>? {
    val gson = Gson()
    if (data == null) {
        return Collections.emptyList()
    }
    val listType= object : TypeToken<List<Product?>?>() {}.type
    return gson.fromJson<List<Product>>(data, listType)
}

    @TypeConverter
    fun myObjectsToStoredString(myObjects: List<Product?>?): String? {
        val gson = Gson()
        return gson.toJson(myObjects)
    }

}