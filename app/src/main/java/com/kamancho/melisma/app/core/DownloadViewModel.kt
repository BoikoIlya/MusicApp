package com.kamancho.melisma.app.core

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamancho.melisma.R
import com.kamancho.melisma.downloader.domain.DownloadInteractor
import com.kamancho.melisma.downloader.presentation.DownloadCompleteCommunication
import com.kamancho.melisma.downloader.presentation.DownloadResult
import kotlinx.coroutines.launch

/**
 * Created by Ilya Boiko @camancho
on 07.12.2023.
 **/
abstract class DownloadViewModel(
 private val globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
 private val connectionChecker: ConnectionChecker,
 private val managerResource: ManagerResource,
 private val downloadInteractor: DownloadInteractor,
 private val downloadCompleteCommunication: DownloadCompleteCommunication,
 private val dispatchersList: DispatchersList,
) : ViewModel() {

    fun download(
     trackUri: Uri?,
     title:String,
     artist: String
    ) = viewModelScope.launch(dispatchersList.io()) {
        val error = downloadInteractor.download(
            trackUri,
            title,
            artist
        )
        if (error.isNotEmpty()) globalSingleUiEventCommunication.map(
            SingleUiEventState.ShowSnackBar.Error(
                error
            )
        )
    }

    fun removeFromDownloads(
     title:String,
     artist: String
    ) = viewModelScope.launch(dispatchersList.io()) {
        val error = downloadInteractor.deleteTrack(title, artist)
        globalSingleUiEventCommunication.map(
            if (error.isEmpty()) {
                downloadCompleteCommunication.map(DownloadResult.Completed)
                SingleUiEventState.ShowSnackBar.Success(managerResource.getString(R.string.success_remove_message))
            } else SingleUiEventState.ShowSnackBar.Error(error)
        )
    }
}