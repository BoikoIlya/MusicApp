package com.example.musicapp.addtoplaylist.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.addtoplaylist.domain.SelectedTracksInteractor
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.FavoritesViewModel
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.creteplaylist.presentation.SelectedTracksCommunication
import com.example.musicapp.favorites.data.SortingState
import com.example.musicapp.favorites.presentation.FavoritesUiState
import com.example.musicapp.main.di.AppModule.Companion.mainPlaylistId
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 16.07.2023.
 **/
class AddToPlaylistViewModel @Inject constructor(
    private val handlerFavoritesUiUpdate: HandleAddToPlaylistTracksUiUpdate,
    private val dispatchersList: DispatchersList,
    private val communication: AddToPlaylistCommunication,
    private val selectedTracksCommunication: SelectedTracksCommunication,
    private val globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
    private val interactor: SelectedTracksInteractor
): ViewModel(), FavoritesViewModel<FavoritesUiState,SelectedTrackUi> {

    private var query: String = ""
    private var sortingState:SortingState = SortingState.ByTime()

    init {
        update(true)
        fetchData()
    }


    fun saveQuery(query: String){
        this.query = query
    }



    fun fetchData(
        sortingState: SortingState = SortingState.ByTime(query)
    ) = viewModelScope.launch(dispatchersList.io()){
        this@AddToPlaylistViewModel.sortingState = sortingState
        communication.showData(interactor.map(sortingState.copyObj(query),mainPlaylistId))
    }


    fun handleItemClick(item: SelectedTrackUi) = viewModelScope.launch(dispatchersList.io()) {
        selectedTracksCommunication.map( interactor.handleItem(item))
        fetchData()
    }

    override fun update(loading: Boolean) {
        viewModelScope.launch(dispatchersList.io()) {
            handlerFavoritesUiUpdate.handle(loading) { interactor.isDbEmpty() }
        }
    }



    override suspend fun collectLoading(
        owner: LifecycleOwner,
        collector: FlowCollector<FavoritesUiState>,
    ) = communication.collectLoading(owner,collector)

    override suspend fun collectState(
        owner: LifecycleOwner,
        collector: FlowCollector<FavoritesUiState>,
    ) = communication.collectState(owner,collector)

    override suspend fun collectData(
        owner: LifecycleOwner,
        collector: FlowCollector<List<SelectedTrackUi>>,
    ) = communication.collectData(owner, collector)


    suspend fun collectGlobalSingleUiEventCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<SingleUiEventState>,
    ) = globalSingleUiEventCommunication.collect(owner,collector)
}