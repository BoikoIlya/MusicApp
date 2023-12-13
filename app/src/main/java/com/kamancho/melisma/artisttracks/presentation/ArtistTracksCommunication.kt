package com.kamancho.melisma.artisttracks.presentation

import androidx.media3.common.MediaItem
import com.kamancho.melisma.favorites.presentation.UiCommunication
import javax.inject.Inject

/**
 * Created by Ilya Boiko @camancho
on 09.12.2023.
 **/
interface ArtistTracksCommunication : UiCommunication<ArtistsTracksUiState, MediaItem> {

    class Base @Inject constructor(
     stateCommunication: ArtistTracksUiStateCommunication,
     tracksCommunication: ArtistsTrackListCommunication,
    ) : ArtistTracksCommunication,
        UiCommunication.Abstract<ArtistsTracksUiState, MediaItem>(
            stateCommunication,
            tracksCommunication
        )
}