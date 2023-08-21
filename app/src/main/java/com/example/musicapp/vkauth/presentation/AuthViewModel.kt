package com.example.musicapp.vkauth.presentation

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.R
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.ManagerResource

import com.example.musicapp.main.data.AuthorizationRepository
import com.example.musicapp.main.presentation.ActivityNavigationCommunication
import com.example.musicapp.main.presentation.ActivityNavigationState
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 17.06.2023.
 **/
class AuthViewModel @Inject constructor(
    private val dispatchersList: DispatchersList,
    private val authorizationRepository: AuthorizationRepository,
    private val managerResource: ManagerResource,
    private val authCommunication: AuthCommunication,
    private val activityNavigationCommunication: ActivityNavigationCommunication
): ViewModel() {


    fun submit(login: String, password: String) = viewModelScope.launch(dispatchersList.io()) {

        if(login.isNotBlank() && password.isNotBlank()) {
            authCommunication.map(AuthUiState.Loading)
            val result = authorizationRepository.token(login.trim(),password.trim())
                if(result.isEmpty()) {
                    activityNavigationCommunication.map(ActivityNavigationState.Empty)
                    authCommunication.map(AuthUiState.Success)
                }
                else
                    authCommunication.map(AuthUiState.Error(result))

        }else authCommunication.map(AuthUiState.Error(managerResource.getString(R.string.fill_all_fields)))

    }


    suspend fun collectAuthState(
        owner: LifecycleOwner,
        collector: FlowCollector<AuthUiState>
    ) = authCommunication.collect(owner, collector)


}