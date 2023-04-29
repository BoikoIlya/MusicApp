package com.example.musicapp.main.data.cache

import android.content.SharedPreferences
import com.example.musicapp.app.core.SharedPref
import javax.inject.Inject

/**
 * Created by HP on 23.04.2023.
 **/

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