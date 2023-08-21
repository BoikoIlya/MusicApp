package com.example.musicapp.frienddetails.presentation

import com.example.musicapp.app.core.Communication
import com.example.musicapp.favorites.presentation.FavoritesUiState
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 19.08.2023.
 **/
interface FriendPlaylistsLoadingCommunication: Communication.Mutable<FavoritesUiState> {

    class Base @Inject constructor(): FriendPlaylistsLoadingCommunication,
        Communication.UiUpdate<FavoritesUiState>(FavoritesUiState.Empty)
}