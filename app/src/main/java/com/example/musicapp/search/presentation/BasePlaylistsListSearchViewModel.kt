package com.example.musicapp.search.presentation

import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.Interactor
import com.example.musicapp.app.core.TrackChecker
import com.example.musicapp.app.core.TracksResultToUiEventCommunicationMapper
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.search.data.SearchRepository
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import com.example.musicapp.userplaylists.presentation.PlaylistUi
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