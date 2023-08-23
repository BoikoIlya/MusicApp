package com.kamancho.melisma.notifications.presentation

import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.app.core.ManagerResource
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.notifications.domain.NotificationDomain
import com.kamancho.melisma.notifications.domain.NotificationResult
import javax.inject.Inject

/**
 * Created by HP on 21.08.2023.
 **/
interface NotificationsResultToUiMapper: NotificationResult.Mapper<Unit> {

    class Base @Inject constructor(
        private val globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
        private val notificationListCommunication: NotificationListCommunication,
        private val notificationsUiStateCommunication: NotificationsUiStateCommunication,
        private val managerResource: ManagerResource,
    ) : NotificationsResultToUiMapper {

        override suspend fun map(data: List<NotificationDomain>, message: String,preResult: Boolean) {
            if(message.isEmpty()){
                val itemsUi = data.map { it.map(managerResource) }
                notificationListCommunication.map(itemsUi)
                if(preResult) return
                if(data.isEmpty())
                    notificationsUiStateCommunication.map(NotificationsUiState.EmptyList)
                else
                    notificationsUiStateCommunication.map(NotificationsUiState.DisableLoading())

            }else{
                globalSingleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(message))
                notificationsUiStateCommunication.map(NotificationsUiState.DisableLoading())
            }
        }
    }


}


