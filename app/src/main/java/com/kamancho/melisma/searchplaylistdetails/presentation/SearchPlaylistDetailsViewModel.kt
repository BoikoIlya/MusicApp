package com.kamancho.melisma.searchplaylistdetails.presentation

import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.FollowPlaylistInteractor
import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.app.core.Interactor
import com.kamancho.melisma.app.core.ManagerResource
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.app.core.TrackChecker
import com.kamancho.melisma.app.core.TracksResultToUiEventCommunicationMapper
import com.kamancho.melisma.favorites.presentation.FavoritesUiState
import com.kamancho.melisma.favorites.presentation.TracksResult
import com.kamancho.melisma.main.data.TemporaryTracksCache
import com.kamancho.melisma.main.presentation.PlayerCommunication
import com.kamancho.melisma.favoritesplaylistdetails.presentation.PlaylistDetailsCommunication
import com.kamancho.melisma.favoritesplaylistdetails.presentation.PlaylistDetailsViewModel
import com.kamancho.melisma.searchplaylistdetails.domain.SearchPlaylistDetailsInteractor
import com.kamancho.melisma.trending.domain.TrackDomain
import com.kamancho.melisma.trending.presentation.SearchPlaylistDetailsResult
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import com.kamancho.melisma.userplaylists.presentation.PlaylistsResultUpdateToUiEventMapper
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