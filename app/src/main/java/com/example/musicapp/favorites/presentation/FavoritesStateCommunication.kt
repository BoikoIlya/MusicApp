package com.example.musicapp.favorites.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface FavoritesStateCommunication: Communication.Mutable<FavoriteTracksUiState>{
    class Base @Inject constructor():
        Communication.UiUpdate<FavoriteTracksUiState>(FavoriteTracksUiState.Loading),
        FavoritesStateCommunication
}
