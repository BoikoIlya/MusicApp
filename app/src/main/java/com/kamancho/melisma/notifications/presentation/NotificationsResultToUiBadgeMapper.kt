package com.kamancho.melisma.notifications.presentation

import com.kamancho.melisma.app.core.ManagerResource
import com.kamancho.melisma.notifications.data.NotificationIdsRepository
import com.kamancho.melisma.notifications.domain.NotificationDomain
import com.kamancho.melisma.notifications.domain.NotificationResult
import javax.inject.Inject

/**
 * Created by HP on 21.08.2023.
 **/
interface NotificationsResultToUiBadgeMapper: NotificationResult.Mapper<Unit> {

    class Base @Inject constructor(
        private val iconBadgeCommunication: NotificationIconBadgeCommunication,
        private val notificationListCommunication: NotificationListCommunication,
        private val notificationsUiStateCommunication: NotificationsUiStateCommunication,
        private val managerResource: ManagerResource,
        private val notificationIdsRepository: NotificationIdsRepository
    ) : NotificationsResultToUiBadgeMapper {

        override suspend fun map(
            data: List<NotificationDomain>,
            message: String,
            preResult: Boolean
        ) {
            if (message.isEmpty()) {
                val itemsUi = data.map { it.map(managerResource) }

                if (itemsUi.any { it is NotificationUi.PrimaryNotification }) {
                    iconBadgeCommunication.map(true)
                }
                else {
                    val result =notificationIdsRepository.containInList(itemsUi.map { it.map() })
                    iconBadgeCommunication.map(result)
                }

                notificationListCommunication.map(itemsUi)
                if (preResult) return
                if (data.isEmpty())
                    notificationsUiStateCommunication.map(NotificationsUiState.EmptyList)
                else
                    notificationsUiStateCommunication.map(NotificationsUiState.DisableLoading())

                notificationIdsRepository.rewrite(itemsUi.map { it.map() })
            } else {
                iconBadgeCommunication.map(false)
                notificationsUiStateCommunication.map(NotificationsUiState.DisableLoading())
            }

        }
    }
}

