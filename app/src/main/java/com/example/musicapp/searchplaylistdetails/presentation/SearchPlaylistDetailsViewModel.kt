package com.example.musicapp.searchplaylistdetails.presentation

import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.R
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.FollowPlaylistInteractor
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.Interactor
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.app.core.TrackChecker
import com.example.musicapp.app.core.TracksResultToUiEventCommunicationMapper
import com.example.musicapp.favorites.presentation.FavoritesUiState
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.favoritesplaylistdetails.presentation.PlaylistDetailsCommunication
import com.example.musicapp.favoritesplaylistdetails.presentation.PlaylistDetailsViewModel
import com.example.musicapp.searchplaylistdetails.domain.SearchPlaylistDetailsInteractor
import com.example.musicapp.trending.domain.TrackDomain
import com.example.musicapp.trending.presentation.SearchPlaylistDetailsResult
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import com.example.musicapp.userplaylists.presentation.PlaylistsResultUpdateToUiEventMapper
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 15.08.2023.
 **/
class SearchPlaylistDetailsViewModel @Inject constructor(
    private val dispatchersList: DispatchersList,
    private val communication: PlaylistDetailsCommunication,
    trackChecker: TrackChecker,
    playerCommunication: PlayerCommunication,
     favoritesInteractor: Interactor<MediaItem, TracksResult>,
    mapper: TracksResultToUiEventCommunicationMapper,
    temporaryTracksCache: TemporaryTracksCache,
//    private val transfer: DataTransfer<PlaylistDomain>,
//    private val toPlaylistUiMapper: PlaylistDomain.Mapper<PlaylistUi>,
//    private val toPlaylistIdMapper: PlaylistUi.Mapper<Int>,
//    private val cachedTracksRepository: CacheRepository<MediaItem>,
//    private val playlistDetailsHandleUiUpdate: PlaylistDetailsHandleUiUpdate,

//    private val handleFavoritesPlaylistTracksFromCache: HandleFavoritesPlaylistTracksFromCache,
//    private val handlePlaylistDataCache: HandlePlaylistDataCache,
//    private val playlistDataCommunication: PlaylistDataCommunication,
//    private val singleUiEventCommunication: GlobalSingleUiEventCommunication,
//    private val mediaItemTransfer: DataTransfer<TrackDomain>,
//    private val toTrackDomainMapper: MediaItemToTrackDomainMapper,

    private val searchPlaylistDetailsInteractor: SearchPlaylistDetailsInteractor,
    private val toUiMapper: SearchPlaylistDetailsResult.Mapper<Unit>,
    private val followPlaylistInteractor: FollowPlaylistInteractor,
    private val playlistsResultUpdateToUiEventMapper: PlaylistsResultUpdateToUiEventMapper,
    private val playlistDomainToUiMapper: PlaylistDomain.Mapper<PlaylistUi>,
    private val trackDomainToUiMapper: TrackDomain.Mapper<MediaItem>,
    private val globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
    private val managerResource: ManagerResource
): PlaylistDetailsViewModel(
    playerCommunication,
    communication,
    temporaryTracksCache,
    dispatchersList,
    favoritesInteractor,
    mapper,
    trackChecker
){

    init {
        communication.showPlaylistData(
            searchPlaylistDetailsInteractor.initialPlaylistData().map(playlistDomainToUiMapper)
        )
        update(false)
    }

    override fun update(loading: Boolean) {
        viewModelScope.launch(dispatchersList.io()) {
            communication.showLoading(FavoritesUiState.Loading)
            searchPlaylistDetailsInteractor.fetch().map(toUiMapper)
            communication.showLoading(FavoritesUiState.DisableLoading)
        }
    }

    fun find(query: String) = viewModelScope.launch(dispatchersList.io()) {
        val result = searchPlaylistDetailsInteractor.find(query)
        if(result.isEmpty()) communication.showUiState(FavoritesUiState.Failure)
        else {
            communication.showData(result.map { it.map(trackDomainToUiMapper) })
            communication.showUiState(FavoritesUiState.Success)
        }
    }

    fun followPlaylist() = viewModelScope.launch(dispatchersList.io()) {
        if(searchPlaylistDetailsInteractor.containsCurrentPlaylist()){
            globalSingleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(managerResource.getString(
                R.string.playlist_already_followed
            )))
            return@launch
        }
        followPlaylistInteractor.followPlaylist(searchPlaylistDetailsInteractor.initialPlaylistData().map(playlistDomainToUiMapper))
            .map(playlistsResultUpdateToUiEventMapper)
    }

}