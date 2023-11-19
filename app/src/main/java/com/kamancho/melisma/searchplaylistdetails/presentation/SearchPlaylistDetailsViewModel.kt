package com.kamancho.melisma.searchplaylistdetails.presentation

import android.util.Log
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
    private val trackDomainToUiMapper: TrackDomain.Mapper<MediaItem>,
    private val globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
    private val managerResource: ManagerResource,
    private val toOwnerIdMapper: PlaylistUi.ToOwnerIdMapper,
    private val toPlaylistIdMapper: PlaylistUi.ToIdMapper,
    private val toTitleMapper: PlaylistUi.ToTitleMapper
): PlaylistDetailsViewModel(
    playerCommunication,
    communication,
    temporaryTracksCache,
    dispatchersList,
    favoritesInteractor,
    mapper,
    trackChecker
){
    private var firstLoadAfterInit = false

    init {
        firstLoadAfterInit = true
    }

    fun initPlaylistData(playlistUi: PlaylistUi, shouldInit: Boolean){
        if(!shouldInit && !firstLoadAfterInit) return
        communication.showPlaylistData(playlistUi)
    }

    override fun update(playlist: PlaylistUi,loading: Boolean,shouldUpdate: Boolean) {
        if(!shouldUpdate && !firstLoadAfterInit) return
        firstLoadAfterInit = false

        viewModelScope.launch(dispatchersList.io()) {
            communication.showLoading(FavoritesUiState.Loading)
            searchPlaylistDetailsInteractor.fetch(
                playlist.map(toOwnerIdMapper),
                playlist.map(toPlaylistIdMapper)
            ).map(toUiMapper)
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

    fun followPlaylist(playlist: PlaylistUi) = viewModelScope.launch(dispatchersList.io()) {
        if(searchPlaylistDetailsInteractor.containsCurrentPlaylist(playlist.map(toTitleMapper))){
            globalSingleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(managerResource.getString(
                R.string.playlist_already_followed
            )))
            return@launch
        }
        followPlaylistInteractor.followPlaylist(playlist)
            .map(playlistsResultUpdateToUiEventMapper)
    }


    override fun onCleared() {
        super.onCleared()
        Log.d("tag", "onCleared: search ")
    }
}