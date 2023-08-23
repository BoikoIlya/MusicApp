package com.kamancho.melisma.favorites.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface FavoritesStateCommunication: Communication.Mutable<FavoritesUiState>{
    class Base @Inject constructor():
        Communication.UiUpdate<FavoritesUiState>(FavoritesUiState.Empty),
        FavoritesStateCommunication
}
