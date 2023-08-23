package com.kamancho.melisma.userplaylists.presentation

import androidx.lifecycle.viewModelScope
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.favorites.presentation.HandleListFromCache
import com.kamancho.melisma.userplaylists.data.FetchPlaylistsRepository
import com.kamancho.melisma.userplaylists.domain.FavoritesPlaylistsInteractor
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import kotlinx.coroutines.flow.collectLatest
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
    private val handleFavoritesListFromCachePlaylistUi: HandleListFromCache<PlaylistUi>
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
            repository.fetchAll(query).collectLatest{list->
                handleFavoritesListFromCachePlaylistUi.handle(list.map { it.map(toPlaylistUiMapper)})
            }
        }
    }

    fun savePlaylist(item: PlaylistUi) = interactor.saveItemToTransfer(item)




}