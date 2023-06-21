package com.example.musicapp.vkauth.presentation

import android.opengl.Visibility
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.R
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.SingleUiEventCommunication
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.favorites.presentation.FavoritesFragment
import com.example.musicapp.main.data.AuthorizationRepository
import com.example.musicapp.main.presentation.BottomNavCommunication
import com.example.musicapp.main.presentation.FragmentManagerCommunication
import com.example.musicapp.main.presentation.FragmentManagerState
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 17.06.2023.
 **/
class AuthViewModel @Inject constructor(
    private val bottomNavCommunication: BottomNavCommunication,
    private val dispatchersList: DispatchersList,
    private val authorizationRepository: AuthorizationRepository,
    private val singleUiEventCommunication: SingleUiEventCommunication,
    private val managerResource: ManagerResource,
    private val authCommunication: AuthCommunication,
    private val fragmentManagerCommunication: FragmentManagerCommunication
): ViewModel() {

    init {
        bottomNavCommunication.map(View.GONE)
    }

    fun submit(login: String, password: String) = viewModelScope.launch(dispatchersList.io()) {

        if(login.isNotBlank() && password.isNotBlank()) {
            authCommunication.map(AuthUiState.Loading)
            val result = authorizationRepository.token(login.trim(),password.trim())
                if(result.isEmpty()) {
                    fragmentManagerCommunication.map(FragmentManagerState.Replace.WithAnimation(FavoritesFragment()))
                    bottomNavCommunication.map(View.VISIBLE)
                }
                else {
                    authCommunication.map(AuthUiState.Error)
                    singleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(result))
                }
        }else singleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(managerResource.getString(
            R.string.fill_all_fields)))

    }


    suspend fun collectAuthState(
        owner: LifecycleOwner,
        collector: FlowCollector<AuthUiState>
    ) = authCommunication.collect(owner, collector)
}