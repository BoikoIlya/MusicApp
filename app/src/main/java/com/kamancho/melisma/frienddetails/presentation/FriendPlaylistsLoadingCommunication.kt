package com.kamancho.melisma.frienddetails.presentation

import com.kamancho.melisma.app.core.Communication
import com.kamancho.melisma.favorites.presentation.FavoritesUiState
import javax.inject.Inject

/**
 * Created by HP on 19.08.2023.
 **/
interface FriendPlaylistsLoadingCommunication: Communication.Mutable<FavoritesUiState> {

    class Base @Inject constructor(): FriendPlaylistsLoadingCommunication,
        Communication.UiUpdate<FavoritesUiState>(FavoritesUiState.Empty)
}