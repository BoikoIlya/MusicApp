package com.kamancho.melisma.notifications.data.cloud

import androidx.annotation.Keep
import com.kamancho.melisma.app.core.ConnectionChecker
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Created by HP on 21.08.2023.
 **/

interface NotificationsFirebaseService {

    suspend fun fetch(): List<NotificationCloud>

    class Base @Inject constructor(
        private val cloudDB: FirebaseFirestore,
        private val connectionChecker: ConnectionChecker
    ): NotificationsFirebaseService {

        companion object{
            @Keep
            private const val notifications_collection = "notifications"
        }



        override suspend fun fetch(): List<NotificationCloud> {
            if(!connectionChecker.isDeviceHaveConnection()) throw UnknownHostException()
            val cloudData = cloudDB.collection(notifications_collection).get().await()
            return cloudData.toObjects(NotificationCloud::class.java)
        }



    }
}