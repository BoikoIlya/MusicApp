package com.kamancho.melisma.vkauth.presentation

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.captcha.data.CaptchaRepository
import com.kamancho.melisma.captcha.presentation.CaptchaFragmentDialog

import com.kamancho.melisma.main.presentation.ActivityNavigationCommunication
import com.kamancho.melisma.main.presentation.ActivityNavigationState
import com.kamancho.melisma.vkauth.domain.AuthResult
import com.kamancho.melisma.vkauth.domain.AuthorizationInteractor
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 17.06.2023.
 **/
class AuthViewModel @Inject constructor(
    private val dispatchersList: DispatchersList,
    private val interactor: AuthorizationInteractor,
    private val authCommunication: AuthCommunication,
    private val singleAuthCommunication: SingleAuthCommunication,
    private val activityNavigationCommunication: ActivityNavigationCommunication,
    private val mapper: AuthResult.Mapper,
): ViewModel() {



    fun submit(login: String, password: String) = viewModelScope.launch(dispatchersList.io()) {
        authCommunication.map(AuthUiState.Loading)
        interactor.token(login, password).map(mapper)
    }

    fun handleUrl(url: String) = viewModelScope.launch(dispatchersList.io()) {
        if(url.contains(access_token) && url.contains(user_id) && !url.contains(auth_redirect)) {
            interactor.saveAccountData(url)
            activityNavigationCommunication.map(ActivityNavigationState.Empty)
            singleAuthCommunication.map(SingleAuthState.LaunchMainActivity)
        }
    }


    companion object{
         const val access_token: String = "access_token"
         const val user_id: String = "user_id"
         const val auth_redirect: String = "auth_redirect"
    }



    suspend fun collectAuthState(
        owner: LifecycleOwner,
        collector: FlowCollector<AuthUiState>
    ) = authCommunication.collect(owner, collector)




}