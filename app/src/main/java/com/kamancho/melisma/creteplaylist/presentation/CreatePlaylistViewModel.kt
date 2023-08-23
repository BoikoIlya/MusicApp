package com.kamancho.melisma.creteplaylist.presentation

import androidx.lifecycle.viewModelScope
import com.kamancho.melisma.addtoplaylist.domain.SelectedTracksInteractor
import com.kamancho.melisma.app.core.CreatePlaylistInteractor
import com.kamancho.melisma.app.core.DispatchersList
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