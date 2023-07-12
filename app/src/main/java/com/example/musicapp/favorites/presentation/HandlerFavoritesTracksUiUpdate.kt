package com.example.musicapp.favorites.presentation

import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.favorites.domain.FavoritesTracksInteractor
import javax.inject.Inject

/**
 * Created by HP on 09.07.2023.
 **/
interface HandlerFavoritesTracksUiUpdate: HandlerFavoritesUiUpdate<FavoritesUiState> {


    class Base @Inject constructor(
        globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
        favoritesTracksCommunication: FavoritesCommunication,
        interactor: FavoritesTracksInteractor
    ): HandlerFavoritesTracksUiUpdate,HandlerFavoritesUiUpdate.Abstract<FavoritesUiState, MediaItem,TracksResult>(
        favoritesTracksCommunication,
        globalSingleUiEventCommunication,
        interactor
    ) {
        override fun successState(): FavoritesUiState = FavoritesUiState.Success

        override fun errorState(): FavoritesUiState = FavoritesUiState.Failure

        override fun loadingState(): FavoritesUiState  = FavoritesUiState.Loading
    }
}