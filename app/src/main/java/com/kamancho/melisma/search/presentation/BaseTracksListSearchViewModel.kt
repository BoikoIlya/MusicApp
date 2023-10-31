package com.kamancho.melisma.search.presentation

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.Interactor
import com.kamancho.melisma.app.core.TrackChecker
import com.kamancho.melisma.app.core.TracksResultToUiEventCommunicationMapper
import com.kamancho.melisma.app.vkdto.TrackItem
import com.kamancho.melisma.favorites.presentation.TracksResult
import com.kamancho.melisma.main.data.TemporaryTracksCache
import com.kamancho.melisma.main.presentation.PlayerCommunication
import com.kamancho.melisma.search.domain.SearchDomainState
import com.kamancho.melisma.search.domain.SearchInteractor
import com.kamancho.melisma.trending.domain.TrackDomain
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import javax.inject.Inject

/**
 * Created by HP on 14.08.2023.
 **/
class BaseTracksListSearchViewModel @Inject constructor(
    private val searchInteractor: SearchInteractor<TrackItem,TrackDomain>,
    mapperToUi: SearchDomainState.Mapper<TrackDomain>,
    private val dataListCommunication: SearchListCommunication<MediaItem>,
    private val loadStateCommunication: SearchPagingLoadStateCommunicationTracks,
    playerCommunication: PlayerCommunication,
    private val dispatchersList: DispatchersList,
    temporaryTracksCache: TemporaryTracksCache,
    private val favoritesInteractor: Interactor<MediaItem, TracksResult>,
    private val mapper: TracksResultToUiEventCommunicationMapper,
    trackChecker: TrackChecker
): SearchListViewModel<TrackItem,TrackDomain,MediaItem>(
    searchInteractor,
    mapperToUi,
    dataListCommunication,
    loadStateCommunication,
    playerCommunication,
    dispatchersList,
    temporaryTracksCache,
    favoritesInteractor,
    mapper,
    trackChecker
)