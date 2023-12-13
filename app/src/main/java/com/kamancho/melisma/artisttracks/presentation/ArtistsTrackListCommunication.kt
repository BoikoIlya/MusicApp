package com.kamancho.melisma.artisttracks.presentation

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by Ilya Boiko @camancho
on 09.12.2023.
 **/
interface ArtistsTrackListCommunication: Communication.Mutable<List<MediaItem>> {

 class Base @Inject constructor(): ArtistsTrackListCommunication, Communication.UiUpdate<List<MediaItem>>(
  emptyList())
}