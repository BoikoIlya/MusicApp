package com.example.musicapp.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.R
import com.example.musicapp.app.core.ConnectionChecker
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.hlscachesystem.data.HlsCacheQueueStore
import com.example.musicapp.hlscachesystem.data.HlsDownloaderCacheRepository
import com.example.musicapp.hlscachesystem.presentation.HLSCachingResult
import com.example.musicapp.hlscachesystem.presentation.HLSCachingResultCommunication
import com.example.musicapp.trending.domain.TrackDomain
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 19.07.2023.
 **/
class FavoritesBottomSheetMenuViewModel @Inject constructor(
    private val resetSwipeActionCommunication: ResetSwipeActionCommunication,
    private val dispatchersList: DispatchersList,
    private val hlsCacheQueueStore: HlsCacheQueueStore,
    private val transfer: DataTransfer<TrackDomain>,
    private val mapper: TrackDomain.Mapper<MediaItem>,
    private val globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
    private val connectionChecker: ConnectionChecker,
    private val managerResource: ManagerResource,
    private val hlsCachingCompleteCommunication: HLSCachingResultCommunication,
    private val hlsCacheRepository: HlsDownloaderCacheRepository
): ViewModel() {

    fun readMediaItem() = transfer.read()!!.map(mapper)

    fun resetSwipedItem() = viewModelScope.launch(dispatchersList.io()) {
        resetSwipeActionCommunication.map(Unit)
    }

    fun saveToCache() = viewModelScope.launch(dispatchersList.io()){
        if(!connectionChecker.isDeviceHaveConnection()) {
            globalSingleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(managerResource.getString(
                R.string.no_connection_message)))
            return@launch
        }
        hlsCacheQueueStore.add(readMediaItem())
        globalSingleUiEventCommunication.map(SingleUiEventState.LaunchHLSCacheService)
        globalSingleUiEventCommunication.map(
            SingleUiEventState.ShowSnackBar.Success(
                managerResource.getString(R.string.caching_started)
            ))
    }

    fun removeFromCache() = viewModelScope.launch(dispatchersList.io()) {
        hlsCacheRepository.remove(readMediaItem())
        hlsCachingCompleteCommunication.map(HLSCachingResult.Completed)
        globalSingleUiEventCommunication.map(
            SingleUiEventState.ShowSnackBar.Success(
                managerResource.getString(R.string.success_remove_message)
            ))
    }
}