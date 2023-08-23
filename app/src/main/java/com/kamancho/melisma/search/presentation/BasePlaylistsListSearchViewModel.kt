package com.kamancho.melisma.search.presentation

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.DataTransfer
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.Interactor
import com.kamancho.melisma.app.core.TrackChecker
import com.kamancho.melisma.app.core.TracksResultToUiEventCommunicationMapper
import com.kamancho.melisma.favorites.presentation.TracksResult
import com.kamancho.melisma.main.data.TemporaryTracksCache
import com.kamancho.melisma.main.presentation.PlayerCommunication
import com.kamancho.melisma.search.data.SearchRepository
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 14.08.2023.
 **/

class BasePlaylistsListSearchViewModel @Inject constructor(
    searchRepository: SearchRepository<PlaylistUi>,
    playerCommunication: PlayerCommunication,
    dispatchersList: DispatchersList,
    temporaryTracksCache: TemporaryTracksCache,
    favoritesInteractor: Interactor<MediaItem, TracksResult>,
    mapper: TracksResultToUiEventCommunicationMapper,
    private val transfer: DataTransfer<PlaylistDomain>,
    private val toPlaylistDomainMapper: PlaylistUi.Mapper<PlaylistDomain>
): SearchListViewModel<PlaylistUi>(
    searchRepository,
    playerCommunication,
    dispatchersList,
    temporaryTracksCache,
    favoritesInteractor,
    mapper,
    TrackChecker.Empty
){

    fun savePlaylistData(data: PlaylistUi){
        transfer.save(data.map(toPlaylistDomainMapper))
    }
}