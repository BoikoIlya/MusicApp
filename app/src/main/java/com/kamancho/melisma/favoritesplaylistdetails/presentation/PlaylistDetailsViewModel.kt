package com.kamancho.melisma.favoritesplaylistdetails.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.BaseViewModel
import com.kamancho.melisma.app.core.CacheFetcher
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.app.core.Interactor
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.app.core.TrackChecker
import com.kamancho.melisma.app.core.TracksResultToUiEventCommunicationMapper
import com.kamancho.melisma.downloader.presentation.DownloadCompleteCommunication
import com.kamancho.melisma.downloader.presentation.DownloadResult
import com.kamancho.melisma.favorites.presentation.FavoritesUiState
import com.kamancho.melisma.favorites.presentation.TracksResult
import com.kamancho.melisma.main.data.TemporaryTracksCache
import com.kamancho.melisma.main.presentation.PlayerCommunication
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

/**
 * Created by HP on 15.08.2023.
 **/
abstract class PlaylistDetailsViewModel(
    playerCommunication: PlayerCommunication,
    private val communication: PlaylistDetailsCommunication,
    temporaryTracksCache: TemporaryTracksCache,
    private val dispatchersList: DispatchersList,
    favoritesInteractor: Interactor<MediaItem, TracksResult>,
    mapper: TracksResultToUiEventCommunicationMapper,
    trackChecker: TrackChecker,
    private val downloadCompleteCommunication: DownloadCompleteCommunication,
    private val singleUiEventCommunication: GlobalSingleUiEventCommunication,
): BaseViewModel<FavoritesUiState>(
    playerCommunication,
    communication,
    temporaryTracksCache,
    dispatchersList,
    favoritesInteractor,
    mapper,
    trackChecker
), CacheFetcher {

    init {
        communication.showData(emptyList())
    }

    override fun checkAndAddTrackToFavorites(item: MediaItem): Job {
        //because first 9 digits is tracks id and other is playlist id, that was add
        //to avoid tracks collision, because ids is not unic
        val newItem = item.buildUpon().setMediaId(item.mediaId.take(9)).build()
        return super.checkAndAddTrackToFavorites(newItem)
    }

    fun sendOneTimeEvent(event: SingleUiEventState) = viewModelScope.launch(dispatchersList.io()) {
        singleUiEventCommunication.map(event)
    }

    override fun clearDownloadCommunication() = downloadCompleteCommunication.map(DownloadResult.Empty)

    override suspend fun collectLoading(
        owner: LifecycleOwner,
        collector: FlowCollector<FavoritesUiState>
    ) = communication.collectLoading(owner, collector)

    suspend fun collectPlaylistDataCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<PlaylistUi>
    ) = communication.collectPlaylistDetails(owner, collector)

    suspend fun collectDownloadCompleteCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<DownloadResult>
    ) = downloadCompleteCommunication.collect(owner,collector)
}