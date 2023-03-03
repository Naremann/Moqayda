package com.example.moqayda

import androidx.room.TypeConverter
import com.example.moqayda.models.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

object ProductTypeConvert{
@TypeConverter
fun storedStringToMyObjects(data: String?): Product? {
    val gson = Gson()
    if (data == null) {
        return Product()
    }
    val listType = object : TypeToken<List<Product?>?>() {}.type
    return gson.fromJson<Product?>(data, listType)
}

@TypeConverter
fun myObjectsToStoredString(myObjects: Product?): String? {
    val gson = Gson()
    return gson.toJson(myObjects)
}

}