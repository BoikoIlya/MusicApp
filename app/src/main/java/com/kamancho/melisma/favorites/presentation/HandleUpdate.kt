package com.kamancho.melisma.favorites.presentation


import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.app.core.UpdateInteractor

/**
 * Created by HP on 09.07.2023.
 **/
interface HandleUpdate {

    suspend fun handle(
        loading: Boolean,
        isDbEmpty: suspend ()->Boolean
    )

    suspend fun handle(
        loading: Boolean,
        id: String,
        isDbEmpty: suspend ()->Boolean,
    )

    suspend fun handle(
        loading: Boolean,
        playlistId: String,
        ownerId: Int,
        isDbEmpty: suspend ()->Boolean,
    )

    abstract class Abstract<M> (
        private val uiStateCommunication:FavoritesUiCommunication<M>,
        private val globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
        private val interactor: UpdateInteractor
    ): HandleUpdate{

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

        override suspend fun handle(
            loading: Boolean,
            id: String,
            isDbEmpty: suspend () -> Boolean,

        ) {
            if(isDbEmpty.invoke() || loading) uiStateCommunication.showLoading(FavoritesUiState.Loading)
            val errorMessage = interactor.update(id)
            if (errorMessage.isNotEmpty())
                globalSingleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(errorMessage))

            uiStateCommunication.showLoading(FavoritesUiState.DisableLoading)
        }

        override suspend fun handle(
            loading: Boolean,
            playlistId: String,
            ownerId: Int,
            isDbEmpty: suspend () -> Boolean
        ) {
            if(isDbEmpty.invoke() || loading) uiStateCommunication.showLoading(FavoritesUiState.Loading)
            val errorMessage = interactor.update(ownerId, playlistId)
            if (errorMessage.isNotEmpty())
                globalSingleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(errorMessage))

            uiStateCommunication.showLoading(FavoritesUiState.DisableLoading)
        }

    }
}