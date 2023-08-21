package com.example.musicapp.favoritesplaylistdetails.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.BaseViewModel
import com.example.musicapp.app.core.CacheRepository
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.Interactor
import com.example.musicapp.app.core.MediaItemToTrackDomainMapper
import com.example.musicapp.app.core.TrackChecker
import com.example.musicapp.app.core.TracksResultToUiEventCommunicationMapper
import com.example.musicapp.favorites.presentation.FavoritesUiState
import com.example.musicapp.favorites.presentation.ResetSwipeActionCommunication
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.trending.domain.TrackDomain
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector

/**
 * Created by HP on 15.08.2023.
 **/
abstract class PlaylistDetailsViewModel(
    playerCommunication: PlayerCommunication,
    private val communication: PlaylistDetailsCommunication,
    temporaryTracksCache: TemporaryTracksCache,
    dispatchersList: DispatchersList,
    favoritesInteractor: Interactor<MediaItem, TracksResult>,
    mapper: TracksResultToUiEventCommunicationMapper,
    trackChecker: TrackChecker
): BaseViewModel<FavoritesUiState>(
    playerCommunication,
    communication,
    temporaryTracksCache,
    dispatchersList,
    favoritesInteractor,
    mapper,
    trackChecker
){

    init {
        communication.showData(emptyList())
    }

    override fun checkAndAddTrackToFavorites(item: MediaItem): Job {
        //because first 9 digits is tracks id and other is playlist id, that was add
        //to avoid tracks collision, because ids is not unic
        val newItem = item.buildUpon().setMediaId(item.mediaId.take(9)).build()
        return super.checkAndAddTrackToFavorites(newItem)
    }

    override suspend fun collectLoading(
        owner: LifecycleOwner,
        collector: FlowCollector<FavoritesUiState>
    ) = communication.collectLoading(owner, collector)

    suspend fun collectPlaylistDataCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<PlaylistUi>
    ) = communication.collectPlaylistDetails(owner, collector)


}