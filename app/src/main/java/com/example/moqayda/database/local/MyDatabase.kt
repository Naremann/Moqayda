package com.example.moqayda.database.local

import android.content.Context
import androidx.room.*
import com.example.moqayda.ProductTypeConvert
import com.example.moqayda.models.CategoryItem

@Database(entities = [CategoryItem::class], version = 5, exportSchema = false)
@TypeConverters(ProductTypeConvert::class)
abstract class MyDatabase:RoomDatabase(){

    abstract fun categoryDao(): CategoryDao
    companion object{
        private const val DATABASE_NAME = "database"
        var database : MyDatabase? = null
        fun init(context:Context) {
            if(database == null){
                database = Room.databaseBuilder(
                    context,
                    MyDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            //Log.e("init", "database:$database")

        }
        fun getInstance() : MyDatabase?{
            return database
        }

    }
}