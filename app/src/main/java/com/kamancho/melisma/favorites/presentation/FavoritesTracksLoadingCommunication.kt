package com.kamancho.melisma.favorites.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 25.07.2023.
 **/
interface FavoritesTracksLoadingCommunication: Communication.Mutable<FavoritesUiState> {

   open class Base @Inject constructor(): FavoritesTracksLoadingCommunication, Communication.UiUpdate<FavoritesUiState>(
        FavoritesUiState.Empty
    )
}