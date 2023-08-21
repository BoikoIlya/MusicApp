package com.example.musicapp.favoritesplaylistdetails.presentation

import com.example.musicapp.app.core.Communication
import com.example.musicapp.favorites.presentation.FavoritesUiState
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface PlaylistDetailsStateCommunication: Communication.Mutable<FavoritesUiState>{
    class Base @Inject constructor():
        Communication.UiUpdate<FavoritesUiState>(FavoritesUiState.Empty),
        PlaylistDetailsStateCommunication
}