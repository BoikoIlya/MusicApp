package com.kamancho.melisma.notifications.data

import com.kamancho.melisma.notifications.data.cache.NotificationsIdsDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Created by HP on 21.08.2023.
 **/
interface NotificationIdsRepository {

    suspend fun containInList(listOfNotificationsId: List<String>): Boolean

    suspend fun rewrite(list: List<String>)

    class Base @Inject constructor(
        private val idsDataStore: NotificationsIdsDataStore
    ): NotificationIdsRepository {

        override suspend fun containInList(listOfNotificationsId: List<String>): Boolean {
            return idsDataStore.read().first().any { it !in listOfNotificationsId }
        }

        override suspend fun rewrite(list: List<String>)  {
            idsDataStore.save(list.toSet())
        }

    }
}