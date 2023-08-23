package com.example.musicapp.notifications.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 21.08.2023.
 **/
interface NotificationsUiStateCommunication: Communication.Mutable<NotificationsUiState> {

    class Base @Inject constructor(): NotificationsUiStateCommunication, Communication.UiUpdate<NotificationsUiState>(NotificationsUiState.EmptyList)
}