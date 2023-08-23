package com.kamancho.melisma.notifications.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.notifications.domain.NotificationsInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 21.08.2023.
 **/
class NotificationsViewModel @Inject constructor(
    private val interactor: NotificationsInteractor,
    private val mapper: NotificationsResultToUiMapper,
    private val notificationListCommunication: NotificationListCommunication,
    private val notificationsUiStateCommunication: NotificationsUiStateCommunication,
    private val dispatcherList: DispatchersList,
    private val notificationIconBadgeCommunication: NotificationIconBadgeCommunication
): ViewModel() {



    private var updateJob: Job? =null
    fun update() {
        notificationsUiStateCommunication.map(NotificationsUiState.Loading)
        updateJob?.cancel()

       updateJob = viewModelScope.launch(dispatcherList.io()) {
            interactor.fetch().collectLatest{
                it.map(mapper)
            }
        }
    }

    fun hideNotificationsBadge() = notificationIconBadgeCommunication.map(false)

    suspend fun collectUiState(
        owner: LifecycleOwner,
        collector: FlowCollector<NotificationsUiState>
    ) = notificationsUiStateCommunication.collect(owner,collector)

    suspend fun collectNotifications(
        owner: LifecycleOwner,
        collector: FlowCollector<List<NotificationUi>>
    ) = notificationListCommunication.collect(owner,collector)
}