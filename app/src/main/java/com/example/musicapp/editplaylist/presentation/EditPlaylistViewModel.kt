package com.example.musicapp.editplaylist.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.example.musicapp.R
import com.example.musicapp.addtoplaylist.domain.SelectedTracksInteractor
import com.example.musicapp.addtoplaylist.presentation.SelectedTrackUi
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.EditPlaylistInteractor
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.creteplaylist.presentation.BasePlaylistDataViewModel
import com.example.musicapp.creteplaylist.presentation.PlaylistDataResultMapper
import com.example.musicapp.creteplaylist.presentation.PlaylistDataSaveBtnUiState
import com.example.musicapp.creteplaylist.presentation.PlaylistDataUiState
import com.example.musicapp.creteplaylist.presentation.PlaylistDataUiStateCommunication
import com.example.musicapp.creteplaylist.presentation.PlaylistSaveBtnUiStateCommunication
import com.example.musicapp.creteplaylist.presentation.SelectedTracksCommunication
import com.example.musicapp.creteplaylist.presentation.SelectedTracksStore
import com.example.musicapp.editplaylist.domain.PlaylistTracksInteractor
import com.example.musicapp.favorites.data.SortingState
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 18.07.2023.
 **/
class EditPlaylistViewModel @Inject constructor(
    private val selectedTracksCommunication: SelectedTracksCommunication,
    private val selectedTracksStore: SelectedTracksStore,
    private val dispatchersList: DispatchersList,
    private val playlistSaveBtnUiStateCommunication: PlaylistSaveBtnUiStateCommunication,
    private val playlistUiStateCommunication: PlaylistDataUiStateCommunication,
    transfer: DataTransfer<PlaylistDomain>,
    toPlaylistUiMapper: PlaylistDomain.Mapper<PlaylistUi>,
    private val titleStateCommunication: TitleStateCommunication,
    private val mapper: PlaylistUi.Mapper<Int>,
    private val playlistTracksInteractor: PlaylistTracksInteractor,
    private val interactor: EditPlaylistInteractor,
    private val playlistResultEditPlaylistUpdateMapper: PlaylistResultEditPlaylistUpdateMapper,
    private val selectedTracksInteractor: SelectedTracksInteractor,
    private val playlistDataResultMapper: PlaylistDataResultMapper,
    private val managerResource: ManagerResource
): BasePlaylistDataViewModel(
    selectedTracksCommunication,
    playlistSaveBtnUiStateCommunication,
    playlistUiStateCommunication,
    dispatchersList,
    selectedTracksInteractor
){
    private var currentPlaylist: PlaylistUi = transfer.read()!!.map(toPlaylistUiMapper)
    private val initialTrackList = emptyList<SelectedTrackUi>().toMutableList()

    init {
        playlistSaveBtnUiStateCommunication.map(PlaylistDataSaveBtnUiState.Hide)
        updateData()
    }

    fun updateData() = viewModelScope.launch(dispatchersList.io()) {
        playlistUiStateCommunication.map(PlaylistDataUiState.Loading)
        playlistTracksInteractor.fetch(currentPlaylist.map(mapper))
            .map(playlistResultEditPlaylistUpdateMapper)
        fetchTracks()
    }

    fun fetchPlaylistData() = currentPlaylist

    fun fetchTracks() = viewModelScope.launch(dispatchersList.io()) {
       val tracks = selectedTracksInteractor.map(
           SortingState.ByTime(""),
           currentPlaylist.map(mapper)
       )
        initialTrackList.apply {
            clear()
            addAll(tracks)
        }
        selectedTracksCommunication.map(tracks)
        selectedTracksInteractor.saveList(tracks)
    }

    fun verify(title: String,description: String) = viewModelScope.launch(dispatchersList.io()) {
        if(title.isBlank()){
            titleStateCommunication.map(TitleUiState.Error(managerResource.getString(R.string.dont_live_empty)))
            playlistSaveBtnUiStateCommunication.map(PlaylistDataSaveBtnUiState.Hide)
            return@launch
        }else titleStateCommunication.map(TitleUiState.Success)

//        val result2 =initialTrackList.size == selectedTracksStore.read().size &&
//                initialTrackList.zip(selectedTracksStore.read()).all { first,second-> first. }

        if(currentPlaylist.map(PlaylistUi.IsPlaylistDataChanged(title,description)) ||
            initialTrackList.toSet()!= selectedTracksInteractor.selectedTracks().toSet()           //compare without taking order into account
        ){
            playlistSaveBtnUiStateCommunication.map(PlaylistDataSaveBtnUiState.Show)
        }else{
            playlistSaveBtnUiStateCommunication.map(PlaylistDataSaveBtnUiState.Hide)
        }

    }

    override fun save(title: String, description: String): Job = viewModelScope.launch(dispatchersList.io()){
        playlistUiStateCommunication.map(PlaylistDataUiState.Loading)
        interactor.editPlaylist(
            currentPlaylist.map(mapper),
            title,
            description,
            initialTrackList
        ).map(playlistDataResultMapper)
    }

    suspend fun collectTitleStateCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<TitleUiState>
    ) = titleStateCommunication.collect(owner,collector)
}