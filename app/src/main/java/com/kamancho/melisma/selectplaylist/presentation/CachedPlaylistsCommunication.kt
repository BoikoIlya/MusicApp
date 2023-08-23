package com.kamancho.melisma.selectplaylist.presentation

import com.kamancho.melisma.app.core.Communication
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 16.07.2023.
 **/
interface CachedPlaylistsCommunication: Communication.Mutable<List<PlaylistUi>> {

    class Base @Inject constructor(): CachedPlaylistsCommunication,Communication.UiUpdate<List<PlaylistUi>>(
        emptyList())
}