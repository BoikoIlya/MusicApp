package com.example.musicapp.favorites.presentation


import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.Interactor
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.app.core.UpdateInteractor

/**
 * Created by HP on 09.07.2023.
 **/
interface HandlerFavoritesUiUpdate {

    suspend fun handle(
        loading: Boolean,
        isDbEmpty: suspend ()->Boolean
    )

    abstract class Abstract<M,E> (
        private val uiStateCommunication:FavoritesUiCommunication<M>,
        private val globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
        private val interactor: UpdateInteractor
    ): HandlerFavoritesUiUpdate{

        override suspend fun handle(
            loading: Boolean,
            isDbEmpty: suspend ()->Boolean
        ) {
            if(isDbEmpty.invoke() || loading) uiStateCommunication.showLoading(FavoritesUiState.Loading)
            val errorMessage = interactor.updateData()
            if (errorMessage.isNotEmpty())
                globalSingleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(errorMessage))

            uiStateCommunication.showLoading(FavoritesUiState.DisableLoading)
        }

    }
}