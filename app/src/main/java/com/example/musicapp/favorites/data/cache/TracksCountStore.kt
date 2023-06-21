package com.example.musicapp.favorites.data.cache

import android.content.SharedPreferences
import com.example.musicapp.app.core.SharedPref
import com.example.musicapp.main.data.cache.TokenStore
import javax.inject.Inject

/**
 * Created by HP on 20.06.2023.
 **/
interface TracksCountStore: SharedPref<Int> {

    fun difference(newCount: Int): Int

    class Base @Inject constructor(
        private val key: String,
        private val store: SharedPreferences
    ): TracksCountStore, SharedPref.SharedPrefInt(key,store){

        override fun difference(newCount: Int) = newCount - read()

    }


}