package com.example.musicapp.userplaylists.presentation

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.FavoritesViewModel
import com.example.musicapp.favorites.presentation.FavoritesUiState
import com.example.musicapp.favorites.presentation.HandleFavoritesListFromCache
import com.example.musicapp.userplaylists.data.FetchPlaylistsRepository
import com.example.musicapp.userplaylists.domain.FavoritesPlaylistsInteractor
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 13.07.2023.
 **/
class PlaylistsViewModel @Inject constructor(
    private val interactor: FavoritesPlaylistsInteractor,
    handlerFavoritesUiUpdate: HandleFavoritesPlaylistsUiUpdate,
    dispatchersList: DispatchersList,
    private val communication: FavoritesPlaylistsUiCommunication,
    private val toPlaylistUiMapper: PlaylistDomain.Mapper<PlaylistUi>,
    private val handlePlaylistsFetchingFromCache: HandlePlaylistsFetchingFromCache,
    private val repository: FetchPlaylistsRepository,
    private val handleFavoritesListFromCachePlaylistUi: HandleFavoritesListFromCache<PlaylistUi>
): BasePlaylistsViewModel(
    handlerFavoritesUiUpdate,
    dispatchersList,
    communication,
    repository
) {

    init {
        update(false)
        fetch("")
    }

    fun fetch(query: String){
        handlePlaylistsFetchingFromCache.fetch(viewModelScope){
            repository.fetchAll(query).collect{list->
                handleFavoritesListFromCachePlaylistUi.handle(list.map { it.map(toPlaylistUiMapper)})
            }
        }
    }

    fun savePlaylist(item: PlaylistUi) = interactor.saveItemToTransfer(item)




}