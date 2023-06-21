package com.example.musicapp.favorites.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.musicapp.app.core.BaseViewModel
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.Remover
import com.example.musicapp.app.core.SingleUiEventCommunication
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.app.core.TracksResultToUiEventCommunicationMapper
import com.example.musicapp.favorites.data.FavoriteTracksRepository
import com.example.musicapp.favorites.data.SortingState
import com.example.musicapp.favorites.domain.FavoritesInteractor
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.PlayerCommunication
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.measureTimeMillis

/**
 * Created by HP on 20.03.2023.
 **/
class FavoritesViewModel @Inject constructor(
    private val repository: FavoriteTracksRepository,
    private val dispatchersList: DispatchersList,
    private val tracksResultToTracksCommunicationMapper: TracksResultToFavoriteTracksCommunicationMapper,
    private val tracksResultToUiEventCommunicationMapper: TracksResultToUiEventCommunicationMapper,
    private val favoritesTracksCommunication: FavoritesCommunication,
    private val temporaryTracksCache: TemporaryTracksCache,

    private val interactor: FavoritesInteractor,
    private val singleUiEventCommunication: SingleUiEventCommunication,

    favoriteTracksRepository: FavoriteTracksRepository,
    playerCommunication: PlayerCommunication,
): BaseViewModel<FavoriteTracksUiState>(
    playerCommunication,
    favoritesTracksCommunication,
    temporaryTracksCache,
    dispatchersList,
    favoriteTracksRepository,
    tracksResultToUiEventCommunicationMapper
    ), Remover {

    private var sortState: SortingState = SortingState.ByTime()
    private var query: String = ""

    init {
        update()
        fetchData(SortingState.ByTime())
    }

    fun update() = viewModelScope.launch(dispatchersList.io()) {
       // favoritesTracksCommunication.showUiState(FavoriteTracksUiState.Loading)
            val result = interactor.updateData()
            if (result.isNotEmpty()) singleUiEventCommunication.map(
                SingleUiEventState.ShowSnackBar.Error(
                    result
                )
            )
    }

    fun saveQuery(query: String){
        this.query = query
    }

    private var fetching: Job? = null

    fun fetchData(sortingState: SortingState = sortState) {
        Log.d("tag", "fetchData:out")
        fetching?.cancel()
        fetching = viewModelScope.launch(dispatchersList.io()) {
            this@FavoritesViewModel.sortState = sortingState
            repository.fetchData(sortingState.copyObj(query)).collectLatest {
                it.map(tracksResultToTracksCommunicationMapper)
                Log.d("tag", "fetchData: inside")
            }
        }
    }


    override fun removeItem(id: String) = viewModelScope.launch(dispatchersList.io()) {
        repository.removeTrack(id).map(tracksResultToUiEventCommunicationMapper)
    }


}


