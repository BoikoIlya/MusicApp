package com.example.musicapp.main.data.cache

import android.content.SharedPreferences
import com.example.musicapp.app.core.SharedPref
import javax.inject.Inject

/**
 * Created by HP on 20.06.2023.
 **/
interface OwnerIdStore: SharedPref<String> {

    class Base @Inject constructor(
        private val ownerIdKey: String,
        private val store: SharedPreferences
    ): OwnerIdStore, SharedPref.SharedPrefString(ownerIdKey,store)

}