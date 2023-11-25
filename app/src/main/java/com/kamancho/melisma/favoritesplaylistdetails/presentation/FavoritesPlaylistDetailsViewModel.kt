package com.kamancho.melisma.favoritesplaylistdetails.presentation

import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.CacheRepository
import com.kamancho.melisma.app.core.DataTransfer
import com.kamancho.melisma.app.core.DeleteItemDialog
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.app.core.Interactor
import com.kamancho.melisma.app.core.MediaItemToTrackDomainMapper
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.app.core.TrackChecker
import com.kamancho.melisma.app.core.TracksResultToUiEventCommunicationMapper
import com.kamancho.melisma.deletetrackfromplaylist.presentation.DeleteTrackFromPlaylistDialogFragment
import com.kamancho.melisma.favorites.data.SortingState
import com.kamancho.melisma.favorites.presentation.ResetSwipeActionCommunication
import com.kamancho.melisma.favorites.presentation.TracksResult
import com.kamancho.melisma.main.data.TemporaryTracksCache
import com.kamancho.melisma.main.presentation.PlayerCommunication
import com.kamancho.melisma.trending.domain.TrackDomain
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
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
    private val isNotFollowedMapper :PlaylistUi.Mapper<Boolean>,
    private val toOwnerIdMapper: PlaylistUi.Mapper<Int>
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
    private lateinit var playlistUi:PlaylistUi
    private lateinit var playlistId:String
    private var firstLoadAfterInit = false

    init {
        firstLoadAfterInit = true
    }

    override fun update(id: String, loading: Boolean, shouldUpdate: Boolean) {
        if(!shouldUpdate && !firstLoadAfterInit) return

        firstLoadAfterInit = false
        viewModelScope.launch(dispatchersList.io()) {
            playlistDetailsHandleUiUpdate.handle(loading,playlistId,playlistUi.map(toOwnerIdMapper)){
                cachedTracksRepository.isDbEmpty(id)
            }
        }
    }

    fun handlePlaylistData(playlistUi: PlaylistUi, shouldUpdate: Boolean){
        if(!shouldUpdate && !firstLoadAfterInit) return
        this.playlistUi = playlistUi
        this.playlistId = playlistUi.map(toPlaylistIdMapper)

        handlePlaylistDataCache.init(playlistUi)
        fetchFromCache("")
        fetchPlaylistData()
        handlePlaylistDataCache.handle(viewModelScope,playlistId)
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

    fun isNotFollowed(playlist: PlaylistUi,initItemTouchHelper:()->Unit){
        if(playlist.map(isNotFollowedMapper)) initItemTouchHelper.invoke()
    }

    fun addBtnVisibility(playlist: PlaylistUi) = if(playlist.map(isNotFollowedMapper)) View.GONE else View.VISIBLE

    override fun launchDeleteItemDialog(item: MediaItem): Job = viewModelScope.launch(dispatchersList.io()) {
        mediaItemTransfer.save(toTrackDomainMapper.map(item))
        singleUiEventCommunication.map(SingleUiEventState.ShowDialog(DeleteTrackFromPlaylistDialogFragment()))
    }


    suspend fun collectDeleteDialogCommunication(
        owner: LifecycleOwner,
        flowCollector: FlowCollector<Unit>
    ) = resetSwipeActionCommunication.collect(owner,flowCollector)


}