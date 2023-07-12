package com.example.musicapp.main.data.cache

import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.musicapp.app.core.SettingsDataStore
import javax.inject.Inject

/**
 * Created by HP on 20.06.2023.
 **/
interface OwnerIdStore: SettingsDataStore<String> {

    class Base @Inject constructor(
        ownerIdKey: Preferences.Key<String>,
        store: DataStore<Preferences>,
    ): OwnerIdStore, SettingsDataStore.Abstract<String>(ownerIdKey,store,"")

}