package com.example.musicapp.favoritesplaylistdetails.presentation

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.CacheRepository
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.DeleteItemDialog
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.Interactor
import com.example.musicapp.app.core.MediaItemToTrackDomainMapper
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.app.core.TrackChecker
import com.example.musicapp.app.core.TracksResultToUiEventCommunicationMapper
import com.example.musicapp.deletetrackfromplaylist.presentation.DeleteTrackFromPlaylistDialogFragment
import com.example.musicapp.favorites.data.SortingState
import com.example.musicapp.favorites.presentation.ResetSwipeActionCommunication
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.trending.domain.TrackDomain
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 22.05.2023.
 **/
class FavoritesPlaylistDetailsViewModel @Inject constructor(
    private val dispatchersList: DispatchersList,
    private val communication: PlaylistDetailsCommunication,
    trackChecker: TrackChecker,
    playerCommunication: PlayerCommunication,
    favoritesInteractor: Interactor<MediaItem, TracksResult>,
    mapper: TracksResultToUiEventCommunicationMapper,
    private val transfer: DataTransfer<PlaylistDomain>,
    private val toPlaylistUiMapper: PlaylistDomain.Mapper<PlaylistUi>,
    private val toPlaylistIdMapper: PlaylistUi.Mapper<String>,
    private val cachedTracksRepository: CacheRepository<MediaItem>,
    private val playlistDetailsHandleUiUpdate: PlaylistDetailsHandleUiUpdate,
    private val temporaryTracksCache: TemporaryTracksCache,
    private val handleFavoritesPlaylistTracksFromCache: HandleFavoritesPlaylistTracksFromCache,
    private val handlePlaylistDataCache: HandlePlaylistDataCache,
    private val singleUiEventCommunication: GlobalSingleUiEventCommunication,
    private val mediaItemTransfer: DataTransfer<TrackDomain>,
    private val toTrackDomainMapper: MediaItemToTrackDomainMapper,
    private val resetSwipeActionCommunication: ResetSwipeActionCommunication,
    private val isNotFollowedMapper :PlaylistUi.Mapper<Boolean>
): PlaylistDetailsViewModel(
    playerCommunication,
    communication,
    temporaryTracksCache,
    dispatchersList,
    favoritesInteractor,
    mapper,
    trackChecker
), DeleteItemDialog {

    private var fetching: Job? = null
    private val playlistUi = transfer.read()!!.map(toPlaylistUiMapper)
    private val playlistId = playlistUi.map(toPlaylistIdMapper)

    init {
        handlePlaylistDataCache.init(playlistUi)
        fetchFromCache("")
        fetchPlaylistData()
        update(false)
    }

    override fun update(loading: Boolean) {
        viewModelScope.launch(dispatchersList.io()) {
            playlistDetailsHandleUiUpdate.handle(loading){
                cachedTracksRepository.isDbEmpty(playlistId)
            }
        }
    }

    fun fetchFromCache(query: String) {
        fetching?.cancel()

        fetching = viewModelScope.launch(dispatchersList.io()) {
            cachedTracksRepository.fetch(SortingState.ByTime(query),playlistId).collect{
                handleFavoritesPlaylistTracksFromCache.handle(it)
            }
        }
    }

    fun fetchPlaylistData() = handlePlaylistDataCache.handle(viewModelScope,playlistId)

    fun isNotFollowed(initItemTouchHelper:()->Unit){
        if(playlistUi.map(isNotFollowedMapper)) initItemTouchHelper.invoke()
    }

    fun addBtnVisibility() = if(playlistUi.map(isNotFollowedMapper)) View.GONE else View.VISIBLE

    override fun launchDeleteItemDialog(item: MediaItem): Job = viewModelScope.launch(dispatchersList.io()) {
        mediaItemTransfer.save(toTrackDomainMapper.map(item))
        singleUiEventCommunication.map(SingleUiEventState.ShowDialog(DeleteTrackFromPlaylistDialogFragment()))
    }


    suspend fun collectDeleteDialogCommunication(
        owner: LifecycleOwner,
        flowCollector: FlowCollector<Unit>
    ) = resetSwipeActionCommunication.collect(owner,flowCollector)


}