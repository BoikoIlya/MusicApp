package com.kamancho.melisma.notifications.presentation

import androidx.lifecycle.LifecycleOwner
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.notifications.domain.NotificationsInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 21.08.2023.
 **/
interface NotificationsUpdateOnAppStart {

    fun update(scope: CoroutineScope)

    suspend fun collectNotificationBadgeCommunication(owner: LifecycleOwner, collector: FlowCollector<Boolean>)

    class Base @Inject constructor(
        private val dispatchersList: DispatchersList,
        private val notificationsInteractor: NotificationsInteractor,
        private val communication: NotificationIconBadgeCommunication,
        private val mapper:NotificationsResultToUiBadgeMapper
    ): NotificationsUpdateOnAppStart {

        private var job: Job?=null

        override fun update(scope: CoroutineScope) {
            job?.cancel()
            job = scope.launch(dispatchersList.io()) {
              val result = notificationsInteractor.fetch()
               result.collectLatest {
                   it.map(mapper)
               }
           }
        }

        override suspend fun collectNotificationBadgeCommunication(
            owner: LifecycleOwner,
            collector: FlowCollector<Boolean>,
        ) = communication.collect(owner,collector)
    }
}