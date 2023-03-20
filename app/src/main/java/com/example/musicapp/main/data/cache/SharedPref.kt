package com.example.musicapp.main.data.cache

import android.content.SharedPreferences
import javax.inject.Inject

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

interface TokenStore: SharedPref<String> {

    class Base @Inject constructor(
        private val key: String,
        private val store: SharedPreferences
    ): TokenStore, SharedPref.SharedPrefString(key,store){

        companion object{
            private const val bearer = "Bearer "
        }

        override fun read(): String {
            return bearer +super.read()
        }

    }
}

