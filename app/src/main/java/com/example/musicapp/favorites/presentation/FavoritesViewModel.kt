package com.example.musicapp.favorites.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.BaseViewModel
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.DeleteItemDialog
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.app.core.TrackChecker
import com.example.musicapp.app.core.TracksResultToUiEventCommunicationMapper
import com.example.musicapp.favorites.data.SortingState
import com.example.musicapp.favorites.domain.FavoritesTracksInteractor
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.PlayerCommunication
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 20.03.2023.
 **/
class FavoritesViewModel @Inject constructor(
    private val dispatchersList: DispatchersList,
    private val tracksResultToTracksCommunicationMapper: TracksResultToFavoriteTracksCommunicationMapper,
    private val interactor: FavoritesTracksInteractor,
    private val singleUiEventCommunication: GlobalSingleUiEventCommunication,
    private val resetSwipeActionCommunication: ResetSwipeActionCommunication,
    private val handlerFavoritesUiUpdate: HandlerFavoritesTracksUiUpdate,
    trackChecker: TrackChecker,
    playerCommunication: PlayerCommunication,
    tracksResultToUiEventCommunicationMapper: TracksResultToUiEventCommunicationMapper,
    favoritesTracksCommunication: FavoritesCommunication,
    temporaryTracksCache: TemporaryTracksCache,
): BaseViewModel<FavoritesUiState>(
    playerCommunication,
    favoritesTracksCommunication,
    temporaryTracksCache,
    dispatchersList,
    interactor,
    tracksResultToUiEventCommunicationMapper,
    trackChecker
    ), DeleteItemDialog {

    private var sortState: SortingState = SortingState.ByTime()
    private var query: String = ""

    init {
        update(false)
        fetchData(SortingState.ByTime())
    }

    fun update(loading:Boolean) = viewModelScope.launch(dispatchersList.io()) {
        handlerFavoritesUiUpdate.handle(loading)
    }

    fun saveQuery(query: String){
        this.query = query
    }

    private var fetching: Job? = null

    fun fetchData(sortingState: SortingState = sortState) {
        fetching?.cancel()
        fetching = viewModelScope.launch(dispatchersList.io()) {
            this@FavoritesViewModel.sortState = sortingState
            interactor.fetchData(sortingState.copyObj(query)).collectLatest {
                it.map(tracksResultToTracksCommunicationMapper)
            }
        }
    }



    override fun launchDeleteItemDialog(item: MediaItem) = viewModelScope.launch(dispatchersList.io()) {
        interactor.saveDeletingItem(item)
        singleUiEventCommunication.map(SingleUiEventState.ShowDialog(DeleteDialogFragment()))
    }

    suspend fun collectDeleteDialogCommunication(
        owner: LifecycleOwner,
        flowCollector: FlowCollector<Unit>
    ) = resetSwipeActionCommunication.collect(owner,flowCollector)

}


