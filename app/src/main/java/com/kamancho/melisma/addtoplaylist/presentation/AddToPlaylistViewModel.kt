package com.kamancho.melisma.addtoplaylist.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamancho.melisma.addtoplaylist.domain.SelectedTracksInteractor
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.FavoritesViewModel
import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.creteplaylist.presentation.SelectedTracksCommunication
import com.kamancho.melisma.favorites.data.SortingState
import com.kamancho.melisma.favorites.presentation.FavoritesTracksViewModel
import com.kamancho.melisma.favorites.presentation.FavoritesUiState
import com.kamancho.melisma.main.di.AppModule.Companion.mainPlaylistId
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
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
    private val interactor: SelectedTracksInteractor,
    private val handleCachedTracksSelected: HandleCachedTracksSelected
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


    fun fetchData(sortingState: SortingState = SortingState.ByTime(query)) = viewModelScope.launch(dispatchersList.io()) {
            this@AddToPlaylistViewModel.sortingState = sortingState
          val result = interactor.map(sortingState.copyObj(query), mainPlaylistId.toString())
                handleCachedTracksSelected.handle(result)

    }


    fun handleItemClick(item: SelectedTrackUi) = viewModelScope.launch(dispatchersList.io()) {
        selectedTracksCommunication.map( interactor.handleItem(item))
        fetchData()
    }

    override fun update(loading: Boolean) {
        viewModelScope.launch(dispatchersList.io()) {
            handlerFavoritesUiUpdate.handle(loading) { interactor.isDbEmpty() }
            fetchData()
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