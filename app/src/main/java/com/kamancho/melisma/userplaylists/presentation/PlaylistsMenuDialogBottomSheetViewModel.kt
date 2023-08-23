package com.kamancho.melisma.userplaylists.presentation

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.kamancho.melisma.app.core.DataTransfer
import com.kamancho.melisma.app.core.DeleteInteractor
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.favorites.presentation.DialogViewModel
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import com.kamancho.melisma.userplaylists.domain.PlaylistsResult
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
    private val toPlaylistIdMapper: PlaylistUi.Mapper<String>,
    private val canEditMapper: PlaylistUi.Mapper<Boolean>,
    private val canEditPlaylistStateCommunication: CanEditPlaylistStateCommunication,
    private val globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
    dataTransfer: DataTransfer<PlaylistDomain>
): DialogViewModel<PlaylistDomain>(dataTransfer){

    fun launchDeletePlaylistDialog() = viewModelScope.launch(dispatchersList.io()) {
        globalSingleUiEventCommunication.map(SingleUiEventState.ShowDialog(DeletePlaylistDialogFragment()))
    }

    fun deletePlaylist() = viewModelScope.launch(dispatchersList.io()) {
        interactor.deleteData(super.fetchData()!!.map(toPlaylistUiMapper).map(toPlaylistIdMapper).toInt()).map(mapper)
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