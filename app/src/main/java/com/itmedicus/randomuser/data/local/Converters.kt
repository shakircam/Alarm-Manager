package com.itmedicus.randomuser.data.local

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.itmedicus.randomuser.model.Dami
import java.io.ByteArrayOutputStream

class Converters {
    var gson = Gson()

    @TypeConverter
    fun nameToString(result: Dami.Name): String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToName(data: String): Dami.Name {
        val listType = object : TypeToken<Dami.Name>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun imageToString(result: Dami.Picture): String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToImage(data: String): Dami.Picture {
        val listType = object : TypeToken<Dami.Picture>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun locationToString(result: Dami.Location): String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToLocation(data: String): Dami.Location {
        val listType = object : TypeToken<Dami.Location>() {}.type
        return gson.fromJson(data, listType)
    }

}