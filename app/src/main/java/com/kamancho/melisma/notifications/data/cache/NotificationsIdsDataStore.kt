package com.kamancho.melisma.notifications.data.cache

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kamancho.melisma.app.core.SettingsDataStore

/**
 * Created by HP on 21.08.2023.
 **/

interface NotificationsIdsDataStore: SettingsDataStore<Set<String>> {

    class Base(
        notificationsIdsKey: Preferences.Key<Set<String>>,
        store: DataStore<Preferences>
    ): NotificationsIdsDataStore, SettingsDataStore.Abstract<Set<String>>(notificationsIdsKey,store, emptySet())

}