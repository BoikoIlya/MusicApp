package com.example.musicapp.notifications.data.cache

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.musicapp.app.core.SettingsDataStore
import com.example.musicapp.main.data.cache.TokenStore
import javax.inject.Inject

/**
 * Created by HP on 21.08.2023.
 **/

interface NotificationsIdsDataStore: SettingsDataStore<Set<String>> {

    class Base(
        notificationsIdsKey: Preferences.Key<Set<String>>,
        store: DataStore<Preferences>
    ): NotificationsIdsDataStore, SettingsDataStore.Abstract<Set<String>>(notificationsIdsKey,store, emptySet())

}