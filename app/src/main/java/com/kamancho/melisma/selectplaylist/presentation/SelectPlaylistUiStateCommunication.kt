package com.kamancho.melisma.selectplaylist.presentation

import com.kamancho.melisma.app.core.Communication
import com.kamancho.melisma.favorites.presentation.FavoritesUiState
import javax.inject.Inject

/**
 * Created by HP on 16.07.2023.
 **/
interface SelectPlaylistUiStateCommunication: Communication.Mutable<FavoritesUiState> {

    class Base @Inject constructor(): SelectPlaylistUiStateCommunication,
        Communication.UiUpdate<FavoritesUiState>(FavoritesUiState.Empty)
}