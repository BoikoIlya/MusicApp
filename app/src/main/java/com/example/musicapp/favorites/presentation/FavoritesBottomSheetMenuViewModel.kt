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
import com.example.musicapp.downloader.domain.DownloadInteractor
import com.example.musicapp.downloader.presentation.DownloadResult
import com.example.musicapp.downloader.presentation.DownloadCompleteCommunication
import com.example.musicapp.trending.domain.TrackDomain
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 19.07.2023.
 **/
class FavoritesBottomSheetMenuViewModel @Inject constructor(
    private val resetSwipeActionCommunication: ResetSwipeActionCommunication,
    private val dispatchersList: DispatchersList,
    private val transfer: DataTransfer<TrackDomain>,
    private val mapper: TrackDomain.Mapper<MediaItem>,
    private val globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
    private val connectionChecker: ConnectionChecker,
    private val managerResource: ManagerResource,
    private val downloadInteractor: DownloadInteractor,
    private val downloadCompleteCommunication: DownloadCompleteCommunication
): ViewModel() {

    fun readMediaItem() = transfer.read()!!.map(mapper)

    fun resetSwipedItem() = viewModelScope.launch(dispatchersList.io()) {
        resetSwipeActionCommunication.map(Unit)
    }

    fun download() = viewModelScope.launch(dispatchersList.io()){
        if(!connectionChecker.isDeviceHaveConnection()) {
            globalSingleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(managerResource.getString(
                R.string.no_connection_message)))
            return@launch
        }
        val mediaItem = readMediaItem()
        val trackUri = mediaItem.localConfiguration?.uri
        if(trackUri==null){
            globalSingleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(managerResource.getString(
                R.string.unavailable_track)))
            return@launch
        }
       val error = downloadInteractor.download(
            trackUri,
            mediaItem.mediaMetadata.title.toString(),
            mediaItem.mediaMetadata.artist.toString()
        )
        if (error.isNotEmpty()) globalSingleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(error))
    }

    fun removeFromDownloads() = viewModelScope.launch(dispatchersList.io()) {
        val mediaItem = readMediaItem()
       val error = downloadInteractor.deleteTrack(
            mediaItem.mediaMetadata.title.toString(),
            mediaItem.mediaMetadata.artist.toString()
        )
        globalSingleUiEventCommunication.map(
        if(error.isEmpty())
        {
            downloadCompleteCommunication.map(DownloadResult.Completed)
            SingleUiEventState.ShowSnackBar.Success(managerResource.getString(R.string.success_remove_message))
        }
        else SingleUiEventState.ShowSnackBar.Error(error)
        )
    }
}