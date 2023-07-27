package com.example.musicapp.selectplaylist.presentation

import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.Communication
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 16.07.2023.
 **/
interface CachedPlaylistsCommunication: Communication.Mutable<List<PlaylistUi>> {

    class Base @Inject constructor(): CachedPlaylistsCommunication,Communication.UiUpdate<List<PlaylistUi>>(
        emptyList())
}