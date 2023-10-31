package com.kamancho.melisma.search.presentation

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.Communication
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by Ilya Boiko @camancho
on 26.10.2023.
 **/
interface SearchListCommunication<T>: Communication.Mutable<List<T>> {

 class BaseTracks @Inject constructor(): SearchListCommunication<MediaItem>,
  Communication.UiUpdate<List<MediaItem>>(emptyList())

 class BasePlaylists @Inject constructor(): SearchListCommunication<PlaylistUi>,
  Communication.UiUpdate<List<PlaylistUi>>(emptyList())
}