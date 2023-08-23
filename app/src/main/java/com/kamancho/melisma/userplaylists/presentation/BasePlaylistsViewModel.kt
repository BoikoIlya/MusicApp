package com.kamancho.melisma.userplaylists.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.FavoritesViewModel
import com.kamancho.melisma.favorites.presentation.FavoritesUiCommunication
import com.kamancho.melisma.favorites.presentation.FavoritesUiState
import com.kamancho.melisma.userplaylists.data.FetchPlaylistsRepository
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