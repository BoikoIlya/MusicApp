package com.kamancho.melisma.main.data.cache

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kamancho.melisma.app.core.SettingsDataStore

/**
 * Created by HP on 20.06.2023.
 **/
interface OwnerIdStore: SettingsDataStore<String> {

    class Base (
        ownerIdKey: Preferences.Key<String>,
        store: DataStore<Preferences>,
    ): OwnerIdStore, SettingsDataStore.Abstract<String>(ownerIdKey,store,"")

}