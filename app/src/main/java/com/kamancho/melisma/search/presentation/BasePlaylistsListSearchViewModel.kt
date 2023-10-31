package com.kamancho.melisma.search.presentation

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.DataTransfer
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.Interactor
import com.kamancho.melisma.app.core.TrackChecker
import com.kamancho.melisma.app.core.TracksResultToUiEventCommunicationMapper
import com.kamancho.melisma.app.vkdto.SearchPlaylistItem
import com.kamancho.melisma.app.vkdto.TrackItem
import com.kamancho.melisma.favorites.presentation.TracksResult
import com.kamancho.melisma.main.data.TemporaryTracksCache
import com.kamancho.melisma.main.presentation.PlayerCommunication
import com.kamancho.melisma.search.data.SearchRepository
import com.kamancho.melisma.search.domain.SearchDomainState
import com.kamancho.melisma.search.domain.SearchInteractor
import com.kamancho.melisma.trending.domain.TrackDomain
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 14.08.2023.
 **/

class BasePlaylistsListSearchViewModel @Inject constructor(
    private val searchInteractor: SearchInteractor<SearchPlaylistItem,PlaylistDomain>,
    mapperToUi: SearchDomainState.Mapper<PlaylistDomain>,
    private val dataListCommunication: SearchListCommunication<PlaylistUi>,
    private val loadStateCommunication: SearchPagingLoadStateCommunicationPlaylists,
    playerCommunication: PlayerCommunication,
    dispatchersList: DispatchersList,
    temporaryTracksCache: TemporaryTracksCache,
    favoritesInteractor: Interactor<MediaItem, TracksResult>,
    mapper: TracksResultToUiEventCommunicationMapper,
    private val toPlaylistDomainMapper: PlaylistUi.Mapper<PlaylistDomain>
): SearchListViewModel<SearchPlaylistItem,PlaylistDomain,PlaylistUi>(
    searchInteractor,
    mapperToUi,
    dataListCommunication,
    loadStateCommunication,
    playerCommunication,
    dispatchersList,
    temporaryTracksCache,
    favoritesInteractor,
    mapper,
    TrackChecker.Empty
)