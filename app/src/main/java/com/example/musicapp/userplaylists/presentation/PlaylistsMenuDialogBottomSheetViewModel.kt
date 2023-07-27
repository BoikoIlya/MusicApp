package com.example.musicapp.userplaylists.presentation

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.DeleteInteractor
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.favorites.presentation.DialogViewModel
import com.example.musicapp.userplaylists.domain.FavoritesPlaylistsInteractor
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import com.example.musicapp.userplaylists.domain.PlaylistsResult
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 14.07.2023.
 **/
class PlaylistsMenuDialogBottomSheetViewModel @Inject constructor(
    private val interactor: DeleteInteractor<PlaylistsResult>,
    private val dispatchersList: DispatchersList,
    private val mapper: PlaylistsResultUpdateToUiEventMapper,
    private val toPlaylistUiMapper: PlaylistDomain.Mapper<PlaylistUi>,
    private val toPlaylistIdMapper: PlaylistUi.Mapper<Int>,
    private val canEditMapper: PlaylistUi.Mapper<Boolean>,
    private val canEditPlaylistStateCommunication: CanEditPlaylistStateCommunication,
    private val globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
    dataTransfer: DataTransfer<PlaylistDomain>
): DialogViewModel<PlaylistDomain>(dataTransfer){

    fun launchDeletePlaylistDialog() = viewModelScope.launch(dispatchersList.io()) {
        globalSingleUiEventCommunication.map(SingleUiEventState.ShowDialog(DeletePlaylistDialogFragment()))
    }

    fun deletePlaylist() = viewModelScope.launch(dispatchersList.io()) {
        interactor.deleteData(super.fetchData()!!.map(toPlaylistUiMapper).map(toPlaylistIdMapper)).map(mapper)
    }

    fun checkIfCanEdit(){
        if(super.fetchData()!!.map(toPlaylistUiMapper).map(canEditMapper)) {
            canEditPlaylistStateCommunication.map(View.VISIBLE)
        }else{
            canEditPlaylistStateCommunication.map(View.GONE)
        }
    }

    suspend fun collectCanEditCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<Int>
    ) = canEditPlaylistStateCommunication.collect(owner,collector)
}