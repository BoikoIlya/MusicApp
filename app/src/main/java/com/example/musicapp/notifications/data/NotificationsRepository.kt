package com.example.musicapp.notifications.data

import com.example.musicapp.notifications.data.cloud.NotificationCloud
import com.example.musicapp.notifications.data.cloud.NotificationsFirebaseService
import javax.inject.Inject

/**
 * Created by HP on 21.08.2023.
 **/
interface NotificationsRepository {

    suspend fun checkNotificationsCloud(): List<NotificationCloud>

    class Base @Inject constructor(
        private val service: NotificationsFirebaseService
    ): NotificationsRepository {

        override suspend fun checkNotificationsCloud(): List<NotificationCloud> = service.fetch()
    }
}