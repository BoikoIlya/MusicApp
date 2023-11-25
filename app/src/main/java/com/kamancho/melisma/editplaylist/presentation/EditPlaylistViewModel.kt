package com.kamancho.melisma.editplaylist.presentation

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.kamancho.melisma.R
import com.kamancho.melisma.addtoplaylist.domain.SelectedTracksInteractor
import com.kamancho.melisma.addtoplaylist.presentation.SelectedTrackUi
import com.kamancho.melisma.app.core.DataTransfer
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.EditPlaylistInteractor
import com.kamancho.melisma.app.core.ManagerResource
import com.kamancho.melisma.creteplaylist.presentation.BasePlaylistDataViewModel
import com.kamancho.melisma.creteplaylist.presentation.PlaylistDataResultMapper
import com.kamancho.melisma.creteplaylist.presentation.PlaylistDataSaveBtnUiState
import com.kamancho.melisma.creteplaylist.presentation.PlaylistDataUiState
import com.kamancho.melisma.creteplaylist.presentation.PlaylistDataUiStateCommunication
import com.kamancho.melisma.creteplaylist.presentation.PlaylistSaveBtnUiStateCommunication
import com.kamancho.melisma.creteplaylist.presentation.SelectedTracksCommunication
import com.kamancho.melisma.editplaylist.domain.PlaylistDetailsInteractor
import com.kamancho.melisma.favorites.data.SortingState
import com.kamancho.melisma.favoritesplaylistdetails.presentation.HandlePlaylistDataCache
import com.kamancho.melisma.favoritesplaylistdetails.presentation.PlaylistDataCommunication
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
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
    private val titleStateCommunication: TitleStateCommunication,
    private val toIdMapper: PlaylistUi.Mapper<String>,
    private val toOwnerIdMapper: PlaylistUi.Mapper<Int>,
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
    private lateinit var currentPlaylist: PlaylistUi
    private val initialTrackList = emptyList<SelectedTrackUi>().toMutableList()



    fun setupCurrentPlaylist(playlist: PlaylistUi, shouldSetup: Boolean){
        if(!shouldSetup) return
        this.currentPlaylist = playlist

        handlePlaylistDataCache.init(currentPlaylist)
        playlistSaveBtnUiStateCommunication.map(PlaylistDataSaveBtnUiState.Hide)
        updateData()
        fetchPlaylistData()
        fetchTracks()
    }

    fun updateData() = viewModelScope.launch(dispatchersList.io()) {
        playlistUiStateCommunication.map(PlaylistDataUiState.Loading)
        editPlaylistUpdateMapper.map(playlistTracksInteractor.update(currentPlaylist.map(toOwnerIdMapper),currentPlaylist.map(toIdMapper)))
        fetchTracks()
    }

    fun fetchTracks() = viewModelScope.launch(dispatchersList.io()) {
           val selectedTracks = selectedTracksInteractor.map(
                SortingState.ByTime(""),
                currentPlaylist.map(toIdMapper)
            )
                val tracks =  selectedTracks.map {track->
                    track.copy(selectedIconVisibility = View.GONE, backgroundColor = managerResource.getColor(R.color.white))
                }
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