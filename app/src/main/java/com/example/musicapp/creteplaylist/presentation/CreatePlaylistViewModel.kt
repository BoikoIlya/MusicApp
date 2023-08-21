package com.example.musicapp.creteplaylist.presentation

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.addtoplaylist.domain.SelectedTracksInteractor
import com.example.musicapp.addtoplaylist.presentation.SelectedTrackUi
import com.example.musicapp.app.core.CreatePlaylistInteractor
import com.example.musicapp.app.core.DispatchersList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 16.07.2023.
 **/



class CreatePlaylistViewModel @Inject constructor(
    private val selectedTracksCommunication: SelectedTracksCommunication,
    private val dispatchersList: DispatchersList,
    private val playlistSaveBtnUiStateCommunication: PlaylistSaveBtnUiStateCommunication,
    private val playlistUiStateCommunication: PlaylistDataUiStateCommunication,
    private val interactor: CreatePlaylistInteractor,
    private val selectedTracksInteractor: SelectedTracksInteractor,
    private val mapper: PlaylistDataResultMapper
): BasePlaylistDataViewModel(
    selectedTracksCommunication,
    playlistSaveBtnUiStateCommunication,
    playlistUiStateCommunication,
    dispatchersList,
    selectedTracksInteractor
) {


   override fun save(title: String,description: String) = viewModelScope.launch(dispatchersList.io()) {
        playlistSaveBtnUiStateCommunication.map(PlaylistDataSaveBtnUiState.Hide)
        playlistUiStateCommunication.map(PlaylistDataUiState.Loading)

        interactor.createPlaylist(title.trim(),description.trim()).map(mapper)

    }


    fun verifyData(title: String)  {
        if(title.isNotBlank()){
            playlistSaveBtnUiStateCommunication.map(PlaylistDataSaveBtnUiState.Show)
            playlistUiStateCommunication.map(PlaylistDataUiState.Success)
        }else{
            playlistSaveBtnUiStateCommunication.map(PlaylistDataSaveBtnUiState.Hide)
        }
    }





}