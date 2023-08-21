package com.example.musicapp.frienddetails.presentation

import com.example.musicapp.app.core.Communication
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 19.08.2023.
 **/
interface FriendPlaylistsListCommunication: Communication.Mutable<List<PlaylistUi>> {

    class Base @Inject constructor(): FriendPlaylistsListCommunication,
        Communication.UiUpdate<List<PlaylistUi>>(emptyList())
}