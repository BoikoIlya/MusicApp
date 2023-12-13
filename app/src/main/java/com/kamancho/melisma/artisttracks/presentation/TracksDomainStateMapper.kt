package com.kamancho.melisma.artisttracks.presentation

import androidx.media3.common.MediaItem
import com.kamancho.melisma.artisttracks.domain.TracksDomainState
import com.kamancho.melisma.trending.domain.TrackDomain
import javax.inject.Inject

/**
 * Created by Ilya Boiko @camancho
on 09.12.2023.
 **/
class TracksDomainStateMapper @Inject constructor(
 private val communication: ArtistTracksCommunication,
 private val mapper: TrackDomain.Mapper<MediaItem>,
) : TracksDomainState.Mapper<Unit> {

    override suspend fun map(state: TracksDomainState.Success, list: List<TrackDomain>) {
        communication.showData(list.map { it.map(mapper) })
        communication.showUiState(ArtistsTracksUiState.Success)
    }

    override suspend fun map(
        state: TracksDomainState.Failure,
        message: String,
        reloadBtnVisibility: Boolean
    ) {
        communication.showUiState(ArtistsTracksUiState.Failure(message, reloadBtnVisibility))
    }
}