package com.kamancho.melisma.downloader.data.cache

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kamancho.melisma.app.core.SettingsDataStore

/**
 * Created by HP on 23.08.2023.
 **/

interface DownloadFolderPathStore: SettingsDataStore<String> {

    class Base(
        tokenKey: Preferences.Key<String>,
        store: DataStore<Preferences>
    ): DownloadFolderPathStore, SettingsDataStore.Abstract<String>(tokenKey,store,"")

}