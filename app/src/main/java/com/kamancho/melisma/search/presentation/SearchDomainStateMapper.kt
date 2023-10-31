package com.kamancho.melisma.search.presentation

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.Communication
import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.app.core.PagingLoadState
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.main.data.TemporaryTracksCache
import com.kamancho.melisma.search.domain.SearchDomainState
import com.kamancho.melisma.trending.domain.TrackDomain
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by Ilya Boiko @camancho
on 26.10.2023.
 **/
abstract class SearchDomainStateMapper<T,R>(
    private val searchListCommunication: SearchListCommunication<R>,
    private val pagingLoadStateCommunication: Communication.Mutable<PagingLoadState>
): SearchDomainState.Mapper<T> {

    override suspend fun map(state: SearchDomainState.Error<T>, message: String) {
        pagingLoadStateCommunication.map(PagingLoadState.Failure(message))
    }

    override suspend fun map(state: SearchDomainState.Success<T>, list: List<T>) {
        pagingLoadStateCommunication.map(PagingLoadState.Hide)
        searchListCommunication.map(mapToUi(list))
    }

    protected abstract suspend fun mapToUi(data: List<T>): List<R>


    class BaseTracks @Inject constructor(
        private val mapper: TrackDomain.Mapper<MediaItem>,
        searchListCommunication: SearchListCommunication<MediaItem>,
        pagingLoadStateCommunication: SearchPagingLoadStateCommunicationTracks,
    ): SearchDomainStateMapper<TrackDomain,MediaItem>(searchListCommunication,pagingLoadStateCommunication),
        SearchDomainState.Mapper<TrackDomain>{

        override suspend fun mapToUi(data: List<TrackDomain>): List<MediaItem> =
            data.map { it.map(mapper) }
    }

    class BasePlaylists @Inject constructor(
        private val mapper: PlaylistDomain.Mapper<PlaylistUi>,
        searchListCommunication: SearchListCommunication<PlaylistUi>,
        pagingLoadStateCommunication: SearchPagingLoadStateCommunicationPlaylists
    ): SearchDomainStateMapper<PlaylistDomain,PlaylistUi>(searchListCommunication,pagingLoadStateCommunication),
        SearchDomainState.Mapper<PlaylistDomain>{

        override suspend fun mapToUi(data: List<PlaylistDomain>): List<PlaylistUi> =
            data.map { it.map(mapper) }
    }
}