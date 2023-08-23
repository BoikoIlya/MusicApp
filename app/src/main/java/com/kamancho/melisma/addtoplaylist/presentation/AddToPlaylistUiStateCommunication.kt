package com.kamancho.melisma.addtoplaylist.presentation

import com.kamancho.melisma.app.core.Communication
import com.kamancho.melisma.favorites.presentation.FavoritesUiState
import javax.inject.Inject

/**
 * Created by HP on 16.07.2023.
 **/
interface AddToPlaylistUiStateCommunication: Communication.Mutable<FavoritesUiState> {

    class Base @Inject constructor(): AddToPlaylistUiStateCommunication,
        Communication.UiUpdate<FavoritesUiState>(FavoritesUiState.Empty)
}