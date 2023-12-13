package com.kamancho.melisma.artisttracks.presentation

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by Ilya Boiko @camancho
on 09.12.2023.
 **/
interface ArtistTracksUiStateCommunication: Communication.Mutable<ArtistsTracksUiState> {

 class Base @Inject constructor(): ArtistTracksUiStateCommunication, Communication.UiUpdate<ArtistsTracksUiState>(
  ArtistsTracksUiState.Empty)
}