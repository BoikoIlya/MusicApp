package com.example.musicapp.editplaylist.presentation

import android.view.View
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
import com.example.musicapp.editplaylist.domain.PlaylistDetailsInteractor
import com.example.musicapp.favorites.data.SortingState
import com.example.musicapp.favoritesplaylistdetails.presentation.HandlePlaylistDataCache
import com.example.musicapp.favoritesplaylistdetails.presentation.PlaylistDataCommunication
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 18.07.2023.
 **/
class EditPlaylistViewModel @Inject constructor(
    private val selectedTracksCommunication: SelectedTracksCommunication,
    private val dispatchersList: DispatchersList,
    private val playlistSaveBtnUiStateCommunication: PlaylistSaveBtnUiStateCommunication,
    private val playlistUiStateCommunication: PlaylistDataUiStateCommunication,
    transfer: DataTransfer<PlaylistDomain>,
    toPlaylistUiMapper: PlaylistDomain.Mapper<PlaylistUi>,
    private val titleStateCommunication: TitleStateCommunication,
    private val toIdMapper: PlaylistUi.Mapper<String>,
    private val playlistTracksInteractor: PlaylistDetailsInteractor,
    private val interactor: EditPlaylistInteractor,
    private val editPlaylistUpdateMapper: EditPlaylistUpdateMapper,
    private val selectedTracksInteractor: SelectedTracksInteractor,
    private val playlistDataResultMapper: PlaylistDataResultMapper,
    private val managerResource: ManagerResource,
    private val handlePlaylistDataCache: HandlePlaylistDataCache,
    private val playlistDataCommunication: PlaylistDataCommunication,
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
        handlePlaylistDataCache.init(currentPlaylist)
        playlistSaveBtnUiStateCommunication.map(PlaylistDataSaveBtnUiState.Hide)
        updateData()
        fetchPlaylistData()
        fetchTracks()
    }

    fun updateData() = viewModelScope.launch(dispatchersList.io()) {
        playlistUiStateCommunication.map(PlaylistDataUiState.Loading)
        editPlaylistUpdateMapper.map(playlistTracksInteractor.updateData())
        fetchTracks()
    }

    fun fetchTracks() = viewModelScope.launch(dispatchersList.io()) {
         selectedTracksInteractor.map(
           SortingState.ByTime(""),
           currentPlaylist.map(toIdMapper)
       ).collect {
        val tracks =  it.map {track->
            track.copy(selectedIconVisibility = View.GONE, backgroundColor = managerResource.getColor(R.color.white))
           }
            initialTrackList.apply {
                clear()
                addAll(tracks)
            }
        selectedTracksCommunication.map(tracks)
        selectedTracksInteractor.saveList(tracks)
       }

    }

    fun verify(title: String,description: String) = viewModelScope.launch(dispatchersList.io()) {

        if(title.isBlank()){
            titleStateCommunication.map(TitleUiState.Error(managerResource.getString(R.string.dont_live_empty)))
            playlistSaveBtnUiStateCommunication.map(PlaylistDataSaveBtnUiState.Hide)
            return@launch
        }else {
            titleStateCommunication.map(TitleUiState.Success)
        }

        if(currentPlaylist.map(PlaylistUi.IsPlaylistDataChanged(title,description)) ||
            selectedTracksInteractor.selectedTracks().toSet() !=initialTrackList.toSet() //compare without taking order into account
        ){
            playlistSaveBtnUiStateCommunication.map(PlaylistDataSaveBtnUiState.Show)
        }else{
            playlistSaveBtnUiStateCommunication.map(PlaylistDataSaveBtnUiState.Hide)
        }

    }

    override fun save(title: String, description: String): Job = viewModelScope.launch(dispatchersList.io()){
        playlistUiStateCommunication.map(PlaylistDataUiState.Loading)
        interactor.editPlaylist(
            currentPlaylist.map(toIdMapper).toInt(),
            title,
            description,
            initialTrackList
        ).map(playlistDataResultMapper)
    }

    fun setupInitialData(item: PlaylistUi){
        currentPlaylist  = item
    }

    fun fetchPlaylistData() = handlePlaylistDataCache.handle(viewModelScope,currentPlaylist.map(toIdMapper))

    suspend fun collectTitleStateCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<TitleUiState>
    ) = titleStateCommunication.collect(owner,collector)

    suspend fun collectPlaylistDataCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<PlaylistUi>
    ) = playlistDataCommunication.collect(owner, collector)
}