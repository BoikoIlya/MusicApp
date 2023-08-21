package com.example.musicapp.userplaylists.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.FavoritesViewModel
import com.example.musicapp.favorites.presentation.FavoritesUiCommunication
import com.example.musicapp.favorites.presentation.FavoritesUiState
import com.example.musicapp.userplaylists.data.FetchPlaylistsRepository
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Created by HP on 19.07.2023.
 **/
abstract class BasePlaylistsViewModel(
    private val handlerFavoritesUiUpdate: HandleFavoritesPlaylistsUiUpdate,
    private val dispatchersList: DispatchersList,
    private val communication: FavoritesUiCommunication<PlaylistUi>,
    private val repository: FetchPlaylistsRepository
): ViewModel(), FavoritesViewModel<FavoritesUiState, PlaylistUi> {


    final override fun update(loading: Boolean) {
        viewModelScope.launch(dispatchersList.io()) {
            handlerFavoritesUiUpdate.handle(loading) {
                repository.fetchAll("").first().isEmpty() }
        }
    }



    override suspend fun collectData(
        owner: LifecycleOwner,
        collector: FlowCollector<List<PlaylistUi>>,
    ) = communication.collectData(owner,collector)

    override suspend fun collectState(
        owner: LifecycleOwner,
        collector: FlowCollector<FavoritesUiState>,
    ) = communication.collectState(owner,collector)

    override suspend fun collectLoading(
        owner: LifecycleOwner,
        collector: FlowCollector<FavoritesUiState>
    ) = communication.collectLoading(owner, collector)
}