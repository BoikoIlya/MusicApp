package com.example.musicapp.app.core

import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Created by HP on 09.03.2023.
 **/
interface SettingsDataStore<T> {

    suspend fun read(): Flow<T>

    suspend fun save(data: T)

    abstract class Abstract<T>(
        private val key: Preferences.Key<T>,
        private val store: DataStore<Preferences>,
        private val defaultValue: T
    ) : SettingsDataStore<T> {

        // private val preferencesKey = stringPreferencesKey(key)
        override suspend fun read(): Flow<T> = store.data.map { it[key] ?: defaultValue }

        override suspend fun save(data: T) {
            store.edit { prefs ->
                prefs[key] = data
            }
        }


    }
}



