package com.example.musicapp.addtoplaylist.presentation

import com.example.musicapp.app.core.Communication
import com.example.musicapp.favorites.presentation.FavoritesUiState
import javax.inject.Inject

/**
 * Created by HP on 16.07.2023.
 **/
interface AddToPlaylistUiStateCommunication: Communication.Mutable<FavoritesUiState> {

    class Base @Inject constructor(): AddToPlaylistUiStateCommunication,
        Communication.UiUpdate<FavoritesUiState>(FavoritesUiState.Empty)
}