package com.kamancho.melisma.vkauth.presentation

import com.kamancho.melisma.main.presentation.ActivityNavigationCommunication
import com.kamancho.melisma.main.presentation.ActivityNavigationState
import com.kamancho.melisma.vkauth.domain.AuthResult
import javax.inject.Inject

/**
 * Created by HP on 15.09.2023.
 **/
class AuthResultMapper @Inject constructor(
    private val authCommunication: AuthCommunication,
    private val activityNavigationCommunication: ActivityNavigationCommunication,
    private val singleAuthCommunication: SingleAuthCommunication,
): AuthResult.Mapper {


    override suspend fun map(result: AuthResult.Success) {
        activityNavigationCommunication.map(ActivityNavigationState.Empty)
        singleAuthCommunication.map(SingleAuthState.LaunchMainActivity)
    }

    override suspend fun map(result: AuthResult.Error, message: String) {
        authCommunication.map(AuthUiState.Error(message))
    }


    override suspend fun map(result: AuthResult.Redirection, redirectionUrl: String) {
        singleAuthCommunication.map(SingleAuthState.LaunchRedirection(redirectionUrl))
        authCommunication.map(AuthUiState.HideError)
    }
}