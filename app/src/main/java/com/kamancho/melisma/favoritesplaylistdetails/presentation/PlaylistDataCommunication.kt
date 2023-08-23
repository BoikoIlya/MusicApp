package com.kamancho.melisma.favoritesplaylistdetails.presentation

import com.kamancho.melisma.app.core.Communication
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
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