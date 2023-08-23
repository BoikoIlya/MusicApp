package com.kamancho.melisma.favoritesplaylistdetails.presentation

import com.kamancho.melisma.app.core.Communication
import com.kamancho.melisma.favorites.presentation.FavoritesUiState
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface PlaylistDetailsStateCommunication: Communication.Mutable<FavoritesUiState>{
    class Base @Inject constructor():
        Communication.UiUpdate<FavoritesUiState>(FavoritesUiState.Empty),
        PlaylistDetailsStateCommunication
}