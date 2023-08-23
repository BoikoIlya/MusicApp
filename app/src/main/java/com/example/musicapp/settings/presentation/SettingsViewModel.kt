package com.example.musicapp.settings.presentation

import android.os.Environment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.R
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.downloader.data.cache.DownloadFolderPathStore
import com.example.musicapp.downloader.data.cache.DownloadTracksCacheDataSource
import com.example.musicapp.downloader.data.cache.DownloadTracksCacheDataSource.Base.Companion.defaultDownloadsFolderName
import com.example.musicapp.downloader.presentation.DownloadCompleteCommunication
import com.example.musicapp.downloader.presentation.DownloadResult
import com.example.musicapp.main.data.AuthorizationRepository
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.math.log10
import kotlin.math.pow

/**
 * Created by HP on 22.08.2023.
 **/
class SettingsViewModel @Inject constructor(
    private val authorizationRepository: AuthorizationRepository,
    private val dispatchersList: DispatchersList,
    private val pathStore: DownloadFolderPathStore,
    private val communication: DownloadsPathCommunication,
    private val globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
    private val cacheDataSource: DownloadTracksCacheDataSource,
    private val downloadedTracksSizeCommunication: DownloadedTracksSizeCommunication,
    private val managerResource: ManagerResource,
    private val downloadCompleteCommunication: DownloadCompleteCommunication
): ViewModel() {

    companion object{
        private val SAFSignature = "tree/primary:"
    }

    init {
        observeDownloadsPath()
        calculateDownloadsSize()
    }

    fun logout() = viewModelScope.launch(dispatchersList.io()) {
        authorizationRepository.logout()
    }

    fun logoutDialog() = viewModelScope.launch(dispatchersList.io()) {
        globalSingleUiEventCommunication.map(SingleUiEventState.ShowDialog(LogoutDialogFragment()))
    }

    fun deleteDownloadedTracksDialog()  = viewModelScope.launch(dispatchersList.io()) {
        globalSingleUiEventCommunication.map(SingleUiEventState.ShowDialog(DeleteDownloadedTracksDialogFragment()))
    }

    fun deleteDownloadedTracks() = viewModelScope.launch(dispatchersList.io()) {
        cacheDataSource.clearAllTracks()
        calculateDownloadsSize()
        downloadCompleteCommunication.map(DownloadResult.Completed)
        globalSingleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Success(managerResource.getString(R.string.tracks_success_deelted)))
    }

    fun saveNewDownloadsPath(path: String) = viewModelScope.launch(dispatchersList.io()) {
        pathStore.save(path.replace(SAFSignature,""))
        downloadCompleteCommunication.map(DownloadResult.Completed)
    }

    fun observeDownloadsPath() = viewModelScope.launch(dispatchersList.io()) {
        pathStore.read().collectLatest {
            communication.map(
               if(it.isEmpty())
                   Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path+File.separator+defaultDownloadsFolderName
                else it
            )
        }
    }

    fun calculateDownloadsSize() = viewModelScope.launch(dispatchersList.io()) {
       val sizeInBytes =  cacheDataSource.downloadsSize()
        if (sizeInBytes <= 0) {
            downloadedTracksSizeCommunication.map(managerResource.getString(R.string.size) + "0 B")
            return@launch
        }


        val units = listOf("B", "KB", "MB", "GB")
        val digitGroups = (log10(sizeInBytes.toDouble()) / log10(1024.0)).toInt()

        val sizeInCorrectUnit = String.format(
            "%.2f %s", sizeInBytes / 1024.0.pow(digitGroups.toDouble()),
            units[digitGroups]
        )

        downloadedTracksSizeCommunication.map(managerResource.getString(R.string.size)+sizeInCorrectUnit)
    }

    suspend fun collectDownloadsPath(
        owner: LifecycleOwner,
        collector: FlowCollector<String>,
    ) = communication.collect(owner,collector)

    suspend fun collectDownloadsSize(
        owner: LifecycleOwner,
        collector: FlowCollector<String>,
    ) = downloadedTracksSizeCommunication.collect(owner,collector)
}