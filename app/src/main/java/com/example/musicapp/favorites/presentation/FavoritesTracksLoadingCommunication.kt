package com.example.musicapp.favorites.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 25.07.2023.
 **/
interface FavoritesTracksLoadingCommunication: Communication.Mutable<FavoritesUiState> {

    class Base @Inject constructor(): FavoritesTracksLoadingCommunication, Communication.UiUpdate<FavoritesUiState>(
        FavoritesUiState.Empty
    )
}