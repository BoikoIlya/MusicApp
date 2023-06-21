package com.example.musicapp.main.data.cache

import android.content.SharedPreferences
import com.example.musicapp.app.core.SharedPref
import javax.inject.Inject

/**
 * Created by HP on 20.06.2023.
 **/
interface TokenStore: SharedPref<String> {

    class Base @Inject constructor(
        private val tokenKey: String,
        private val store: SharedPreferences
    ): TokenStore, SharedPref.SharedPrefString(tokenKey,store)

}