package com.example.musicapp.updatesystem.data.cache

import android.content.SharedPreferences
import com.example.musicapp.BuildConfig
import com.example.musicapp.app.core.SharedPref
import javax.inject.Inject

/**
 * Created by HP on 23.04.2023.
 **/
interface UpdateDataStore: SharedPref<String> {


    class Version @Inject constructor(
        private val store: SharedPreferences,
        private val key: String
    ): UpdateDataStore,SharedPref.SharedPrefString(key,store){

        override fun read(): String {
            val data = super.read()
            return if(data=="0") return BuildConfig.VERSION_NAME else data
        }
    }

    class ApkUrl @Inject constructor(
        private val store: SharedPreferences,
        private val key: String
    ): UpdateDataStore,SharedPref.SharedPrefString(key,store)

    class Description @Inject constructor(
        private val store: SharedPreferences,
        private val key: String
    ): UpdateDataStore,SharedPref.SharedPrefString(key,store)
}