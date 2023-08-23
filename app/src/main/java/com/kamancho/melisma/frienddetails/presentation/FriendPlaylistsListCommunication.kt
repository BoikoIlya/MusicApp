package com.kamancho.melisma.frienddetails.presentation

import com.kamancho.melisma.app.core.Communication
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 19.08.2023.
 **/
interface FriendPlaylistsListCommunication: Communication.Mutable<List<PlaylistUi>> {

    class Base @Inject constructor(): FriendPlaylistsListCommunication,
        Communication.UiUpdate<List<PlaylistUi>>(emptyList())
}