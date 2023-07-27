package com.example.musicapp.creteplaylist.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.addtoplaylist.domain.SelectedTracksInteractor
import com.example.musicapp.addtoplaylist.presentation.SelectedTrackUi
import com.example.musicapp.app.core.DispatchersList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

/**
 * Created by HP on 26.07.2023.
 **/
interface CollectSelectedTracks{
    suspend fun collectSelectedTracksCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<List<SelectedTrackUi>>
    )

}

abstract class BasePlaylistDataViewModel(
    private val selectedTracksCommunication: SelectedTracksCommunication,
    private val playlistSaveBtnUiStateCommunication: PlaylistSaveBtnUiStateCommunication,
    private val playlistUiStateCommunication: PlaylistDataUiStateCommunication,
    private val dispatchersList: DispatchersList,
    private val interactor: SelectedTracksInteractor
): ViewModel(),CollectSelectedTracks {

    abstract fun save(title: String,description: String): Job

    init {
        clearData()
    }

    fun clearData() = viewModelScope.launch(dispatchersList.io()){
        playlistUiStateCommunication.map(PlaylistDataUiState.Success)
        interactor.saveList(emptyList())
        selectedTracksCommunication.map(emptyList())
    }

    fun removeItem(item: SelectedTrackUi) = viewModelScope.launch(dispatchersList.io()) {
        selectedTracksCommunication.map(interactor.handleItem(item))
    }

    override suspend fun collectSelectedTracksCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<List<SelectedTrackUi>>
    ) = selectedTracksCommunication.collect(owner,collector)

    suspend fun collectSaveButtonState(
        owner: LifecycleOwner,
        collector: FlowCollector<PlaylistDataSaveBtnUiState>
    ) = playlistSaveBtnUiStateCommunication.collect(owner,collector)

    suspend fun collectUiState(
        owner: LifecycleOwner,
        collector: FlowCollector<PlaylistDataUiState>
    ) = playlistUiStateCommunication.collect(owner,collector)
}