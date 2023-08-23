package com.kamancho.melisma.vkauth.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 17.06.2023.
 **/
interface AuthCommunication: Communication.Mutable<AuthUiState> {

    class Base @Inject constructor(): AuthCommunication, Communication.UiUpdate<AuthUiState>(AuthUiState.Empty)
}