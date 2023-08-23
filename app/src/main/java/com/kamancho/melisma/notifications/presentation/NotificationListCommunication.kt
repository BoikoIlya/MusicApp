package com.kamancho.melisma.notifications.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 21.08.2023.
 **/
interface NotificationListCommunication: Communication.Mutable<List<NotificationUi>> {

    class Base @Inject constructor(): NotificationListCommunication,
        Communication.UiUpdate<List<NotificationUi>>(emptyList())
}