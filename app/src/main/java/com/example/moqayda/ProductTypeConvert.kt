package com.example.moqayda

import androidx.room.TypeConverter
import com.example.moqayda.models.test.CategoryProductViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

object ProductTypeConvert{
@TypeConverter
open fun storedStringToMyObjects(data: String?): List<CategoryProductViewModel?>? {
    val gson = Gson()
    if (data == null) {
        return Collections.emptyList()
    }
    val listType= object : TypeToken<List<CategoryProductViewModel?>?>() {}.type
    return gson.fromJson<List<CategoryProductViewModel>>(data, listType)
}

    @TypeConverter
    fun myObjectsToStoredString(myObjects: List<CategoryProductViewModel?>?): String? {
        val gson = Gson()
        return gson.toJson(myObjects)
    }

}