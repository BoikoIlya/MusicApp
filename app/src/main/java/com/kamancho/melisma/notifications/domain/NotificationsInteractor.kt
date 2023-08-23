package com.kamancho.melisma.notifications.domain

import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.HandleResponse
import com.kamancho.melisma.app.core.ManagerResource
import com.kamancho.melisma.notifications.data.NotificationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by HP on 21.08.2023.
 **/
interface NotificationsInteractor {

    suspend fun fetch(): Flow<NotificationResult>


    class Base @Inject constructor(
        private val handleResponse: HandleResponse,
        private val managerResource: ManagerResource,
        private val repository: NotificationsRepository,
        private val checkForPermissions: CheckForPermissions
    ): NotificationsInteractor {

        override suspend fun fetch(): Flow<NotificationResult> = flow {
            handleResponse.handle({
                val updateNotification = NotificationDomain(managerResource.getString(R.string.update_version), managerResource.getString(R.string.update_info), managerResource.getString(R.string.update_title), managerResource.getString(R.string.update_date), NotificationDomain.TYPE_DEFAULT, "", "")
                val notifications = checkForPermissions.check().toMutableList()
                notifications.add(updateNotification)
                emit(NotificationResult.Success(notifications,true))

                val cloudNotifications = repository.checkNotificationsCloud()
                notifications.addAll(cloudNotifications.map{ it.map(Unit)})

                val sortedList = notifications.sortedWith(compareBy<NotificationDomain> { it.notificationType() }
                    .thenByDescending { it.dateFormatted() })

                emit(NotificationResult.Success(sortedList,false))
            },{message,_->
                emit(NotificationResult.Error(message))
            })
        }
    }
}