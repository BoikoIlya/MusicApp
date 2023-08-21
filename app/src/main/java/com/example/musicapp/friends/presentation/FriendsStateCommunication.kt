package com.example.musicapp.friends.presentation

import com.example.musicapp.app.core.Communication
import com.example.musicapp.favorites.presentation.FavoritesUiState
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface FriendsStateCommunication: Communication.Mutable<FavoritesUiState>{
    class Base @Inject constructor():
        Communication.UiUpdate<FavoritesUiState>(FavoritesUiState.Empty),
        FriendsStateCommunication
}