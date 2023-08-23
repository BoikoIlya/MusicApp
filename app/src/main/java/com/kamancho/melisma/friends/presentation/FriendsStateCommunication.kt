package com.kamancho.melisma.friends.presentation

import com.kamancho.melisma.app.core.Communication
import com.kamancho.melisma.favorites.presentation.FavoritesUiState
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface FriendsStateCommunication: Communication.Mutable<FavoritesUiState>{
    class Base @Inject constructor():
        Communication.UiUpdate<FavoritesUiState>(FavoritesUiState.Empty),
        FriendsStateCommunication
}