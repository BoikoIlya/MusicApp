package com.kamancho.melisma.main.data.cache

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kamancho.melisma.app.core.SettingsDataStore

/**
 * Created by HP on 20.06.2023.
 **/
interface TokenStore: SettingsDataStore<String> {

    class Base(
        tokenKey: Preferences.Key<String>,
        store: DataStore<Preferences>
    ): TokenStore, SettingsDataStore.Abstract<String>(tokenKey,store,"")

}