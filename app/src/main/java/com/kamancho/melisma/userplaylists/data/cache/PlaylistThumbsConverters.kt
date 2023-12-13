package com.kamancho.melisma.userplaylists.data.cache

import androidx.annotation.Keep
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

/**
 * Created by HP on 13.07.2023.
 **/

class PlaylistThumbsConverters {

    private val gson = Gson()

    @TypeConverter
    fun fromListString(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toListString(value: String?): List<String> {
        return if (!value.isNullOrEmpty()) {
            try {
                val listType = object : TypeToken<List<String>>() {}.type
                gson.fromJson(value, listType)
            }catch (e: Exception){
                emptyList()
            }
        } else {
            emptyList()
        }
    }
}