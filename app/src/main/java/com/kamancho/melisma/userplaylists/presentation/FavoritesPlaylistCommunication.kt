package com.kamancho.melisma.userplaylists.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 13.07.2023.
 **/
interface FavoritesPlaylistCommunication: Communication.Mutable<List<PlaylistUi>> {

    class Base @Inject constructor(): FavoritesPlaylistCommunication,
        Communication.UiUpdate<List<PlaylistUi>>(emptyList())
}