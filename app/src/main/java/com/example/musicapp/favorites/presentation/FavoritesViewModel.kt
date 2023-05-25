package com.example.musicapp.favorites.presentation

import androidx.lifecycle.viewModelScope
import com.example.musicapp.app.core.BaseViewModel
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.Remover
import com.example.musicapp.app.core.TracksResultToUiEventCommunicationMapper
import com.example.musicapp.favorites.data.FavoriteTracksRepository
import com.example.musicapp.favorites.data.SortingState
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.PlayerCommunication
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

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
        fetchData(SortingState.ByTime())
    }

    fun saveQuery(query: String){
        this.query = query
    }

    private var fetching: Job? = null

    fun fetchData(sortingState: SortingState = sortState) {
        fetching?.cancel()
        fetching = viewModelScope.launch(dispatchersList.io()) {
            this@FavoritesViewModel.sortState = sortingState
            repository.fetchData(sortingState.copyObj(query)).collectLatest {
                it.map(tracksResultToTracksCommunicationMapper)
            }
        }
    }


    override fun removeItem(id: String) = viewModelScope.launch(dispatchersList.io()) {
        repository.removeTrack(id).map(tracksResultToUiEventCommunicationMapper)
    }


}


