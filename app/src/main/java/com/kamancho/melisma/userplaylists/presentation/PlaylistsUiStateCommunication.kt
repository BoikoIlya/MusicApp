package com.kamancho.melisma.userplaylists.presentation

import com.kamancho.melisma.app.core.Communication
import com.kamancho.melisma.favorites.presentation.FavoritesUiState
import javax.inject.Inject

/**
 * Created by HP on 13.07.2023.
 **/
interface PlaylistsUiStateCommunication: Communication.Mutable<FavoritesUiState> {

    class Base @Inject constructor(): PlaylistsUiStateCommunication,
        Communication.UiUpdate<FavoritesUiState>(FavoritesUiState.Empty)
}