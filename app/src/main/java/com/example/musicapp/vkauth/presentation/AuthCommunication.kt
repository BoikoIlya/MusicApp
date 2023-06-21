package com.example.musicapp.vkauth.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 17.06.2023.
 **/
interface AuthCommunication: Communication.Mutable<AuthUiState> {

    class Base @Inject constructor(): AuthCommunication, Communication.UiUpdate<AuthUiState>(AuthUiState.Empty)
}