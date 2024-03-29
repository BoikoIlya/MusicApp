package com.example.musicapp.app.core

import android.content.SharedPreferences

/**
 * Created by HP on 09.03.2023.
 **/
interface SharedPref<T> {

    fun read(): T

    fun save(data: T)

   abstract class SharedPrefString (
        private val key: String,
        private val store: SharedPreferences
    ): SharedPref<String> {

        override fun read(): String = store.getString(key,"0")?: "0"

        override fun save(data: String) {
            val editor = store.edit()
            editor.putString(key, data).apply()
        }


    }
}



