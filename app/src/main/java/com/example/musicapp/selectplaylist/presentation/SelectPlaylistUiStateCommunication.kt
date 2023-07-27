package com.example.musicapp.selectplaylist.presentation

import com.example.musicapp.app.core.Communication
import com.example.musicapp.favorites.presentation.FavoritesUiState
import javax.inject.Inject

/**
 * Created by HP on 16.07.2023.
 **/
interface SelectPlaylistUiStateCommunication: Communication.Mutable<FavoritesUiState> {

    class Base @Inject constructor(): SelectPlaylistUiStateCommunication,
        Communication.UiUpdate<FavoritesUiState>(FavoritesUiState.Empty)
}