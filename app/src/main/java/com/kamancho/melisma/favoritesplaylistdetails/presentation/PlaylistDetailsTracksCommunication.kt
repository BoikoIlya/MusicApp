package com.kamancho.melisma.favoritesplaylistdetails.presentation

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface PlaylistDetailsTracksCommunication: Communication.Mutable<List<MediaItem>>{
    class Base @Inject constructor():
        Communication.UiUpdate<List<MediaItem>>(emptyList()),
        PlaylistDetailsTracksCommunication
}