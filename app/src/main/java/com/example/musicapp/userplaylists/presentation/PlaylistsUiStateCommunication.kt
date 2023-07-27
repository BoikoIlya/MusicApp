package com.example.musicapp.userplaylists.presentation

import com.example.musicapp.app.core.Communication
import com.example.musicapp.favorites.presentation.FavoritesUiState
import javax.inject.Inject

/**
 * Created by HP on 13.07.2023.
 **/
interface PlaylistsUiStateCommunication: Communication.Mutable<FavoritesUiState> {

    class Base @Inject constructor(): PlaylistsUiStateCommunication,
        Communication.UiUpdate<FavoritesUiState>(FavoritesUiState.Empty)
}