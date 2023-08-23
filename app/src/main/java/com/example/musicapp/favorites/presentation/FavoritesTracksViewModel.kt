package com.example.musicapp.favorites.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.R
import com.example.musicapp.app.core.BaseViewModel
import com.example.musicapp.app.core.CacheRepository
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.DeleteItemDialog
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.HandleFavoritesTracksSortedSearch
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.app.core.TrackChecker
import com.example.musicapp.app.core.TracksResultToUiEventCommunicationMapper
import com.example.musicapp.favorites.data.SortingState
import com.example.musicapp.favorites.domain.FavoritesTracksInteractor
import com.example.musicapp.downloader.presentation.DownloadResult
import com.example.musicapp.downloader.presentation.DownloadCompleteCommunication
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.di.AppModule.Companion.mainPlaylistId
import com.example.musicapp.main.presentation.PlayerCommunication
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 20.03.2023.
 **/
class FavoritesTracksViewModel @Inject constructor(
    private val dispatchersList: DispatchersList,
    private val interactor: FavoritesTracksInteractor,
    private val singleUiEventCommunication: GlobalSingleUiEventCommunication,
    private val resetSwipeActionCommunication: ResetSwipeActionCommunication,
    private val handlerFavoritesUiUpdate: HandlerFavoritesTracksUiUpdate,
    trackChecker: TrackChecker,
    playerCommunication: PlayerCommunication,
    tracksResultToUiEventCommunicationMapper: TracksResultToUiEventCommunicationMapper,
    private val favoritesTracksCommunication: FavoritesTracksCommunication,
    private val cachedTracksRepository: CacheRepository<MediaItem>,
    private val handleTracksSortedSearch: HandleFavoritesTracksSortedSearch,
    private val managerResource: ManagerResource,
    private val temporaryTracksCache: TemporaryTracksCache,
    private val downloadCompleteCommunication: DownloadCompleteCommunication
): BaseViewModel<FavoritesUiState>(
    playerCommunication,
    favoritesTracksCommunication,
    temporaryTracksCache,
    dispatchersList,
    interactor,
    tracksResultToUiEventCommunicationMapper,
    trackChecker
    ), DeleteItemDialog {

   private var query: String = ""

    init {
        update(false)
        fetchData(SortingState.ByTime())
    }



    override fun update(loading:Boolean) {
        viewModelScope.launch(dispatchersList.io()) {
            handlerFavoritesUiUpdate.handle(loading) {
                cachedTracksRepository.isDbEmpty(mainPlaylistId.toString())
            }
        }
    }

    fun saveQuery(query: String){
        this.query = query
    }

    fun saveMediaItem(mediaItem: MediaItem){
        interactor.saveItemToTransfer(mediaItem)
    }

    fun fetchData(sortingState: SortingState) =
        handleTracksSortedSearch.handle(sortingState,viewModelScope,query,mainPlaylistId.toString())
    fun fetchData() = handleTracksSortedSearch.handle(viewModelScope,query,mainPlaylistId.toString())

    fun shuffle() = viewModelScope.launch(dispatchersList.io()) {
        val list = temporaryTracksCache.readCurrentPageTracks()
        if(list.isEmpty()){
            singleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(
                managerResource.getString(R.string.no_favorite_tracks)
            ))
            return@launch
        }
        val shuffled = list.shuffled()
        temporaryTracksCache.saveCurrentPageTracks(shuffled)
        playMusic(shuffled.first())
    }

    fun clearDownloadState() = downloadCompleteCommunication.map(DownloadResult.Empty)

    override fun launchDeleteItemDialog(item: MediaItem) = viewModelScope.launch(dispatchersList.io()) {
        interactor.saveItemToTransfer(item)
        singleUiEventCommunication.map(SingleUiEventState.ShowDialog(DeleteTrackDialogFragment()))
    }

    suspend fun collectDeleteDialogCommunication(
        owner: LifecycleOwner,
        flowCollector: FlowCollector<Unit>
    ) = resetSwipeActionCommunication.collect(owner,flowCollector)


    override suspend fun collectLoading(
        owner: LifecycleOwner,
        collector: FlowCollector<FavoritesUiState>
    ) = favoritesTracksCommunication.collectLoading(owner, collector)

    suspend fun collectDownloadCompleteCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<DownloadResult>
    ) = downloadCompleteCommunication.collect(owner, collector)
}


