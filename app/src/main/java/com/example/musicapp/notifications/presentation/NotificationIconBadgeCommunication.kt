package com.example.musicapp.notifications.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 21.08.2023.
 **/
interface NotificationIconBadgeCommunication: Communication.Mutable<Boolean> {

    class Base @Inject constructor(): NotificationIconBadgeCommunication,Communication.UiUpdate<Boolean>(false)
}