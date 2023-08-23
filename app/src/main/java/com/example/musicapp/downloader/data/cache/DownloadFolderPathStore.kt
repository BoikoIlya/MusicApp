package com.example.musicapp.downloader.data.cache

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.musicapp.app.core.SettingsDataStore
import com.example.musicapp.main.data.cache.TokenStore

/**
 * Created by HP on 23.08.2023.
 **/

interface DownloadFolderPathStore: SettingsDataStore<String> {

    class Base(
        tokenKey: Preferences.Key<String>,
        store: DataStore<Preferences>
    ): DownloadFolderPathStore, SettingsDataStore.Abstract<String>(tokenKey,store,"")

}