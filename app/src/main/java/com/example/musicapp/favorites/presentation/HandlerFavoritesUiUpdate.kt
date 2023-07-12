package com.example.musicapp.favorites.presentation


import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.Interactor
import com.example.musicapp.app.core.SingleUiEventState
import javax.inject.Inject

/**
 * Created by HP on 09.07.2023.
 **/
interface HandlerFavoritesUiUpdate<T> {

    suspend fun handle(
        loading: Boolean,
    )

    abstract class Abstract<T,M,E> (
        private val uiStateCommunication: UiCommunication<T>,
        private val globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
        private val interactor: Interactor<M,E>
    ): HandlerFavoritesUiUpdate<T>{

        override suspend fun handle(
            loading: Boolean,
        ) {
            if(interactor.isDBEmpty() || loading) uiStateCommunication.showUiState(loadingState())
            val errorMessage = interactor.updateData()
            if (errorMessage.isNotEmpty()) {
                globalSingleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(errorMessage))
                uiStateCommunication.showUiState(errorState())
            }else uiStateCommunication.showUiState(successState())
        }

        protected abstract fun successState(): T
        protected abstract fun errorState(): T
        protected abstract fun loadingState(): T
    }
}