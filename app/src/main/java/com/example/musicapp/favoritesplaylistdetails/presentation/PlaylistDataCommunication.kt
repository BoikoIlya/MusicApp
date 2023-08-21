package com.example.musicapp.favoritesplaylistdetails.presentation

import com.example.musicapp.app.core.Communication
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface PlaylistDataCommunication: Communication.Mutable<PlaylistUi>{
    class Base @Inject constructor():
        Communication.UiUpdate<PlaylistUi>(PlaylistUi(
            playlistId = "0",
            title = "",
            isFollowing = false,
            count = 0,
            description = "",
            ownerId = 0,
            thumbStates = listOf()
        )),
        PlaylistDataCommunication
}