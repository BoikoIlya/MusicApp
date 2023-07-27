package com.example.musicapp.app.core

import android.util.Log
import androidx.media3.common.MediaItem
import com.example.musicapp.favorites.data.SortingState
import com.example.musicapp.favorites.presentation.HandleFavoritesListFromCache
import com.example.musicapp.favorites.presentation.HandleFavoritesTracksFromCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 16.07.2023.
 **/
interface HandleTracksSortedSearch {

    fun handle(
        sortingState: SortingState,
        scope: CoroutineScope,
        query: String,
        playlistId: Int
    )

    fun handle(
        scope: CoroutineScope,
        query: String,
        playlistId: Int
    )

    abstract class Abstract: HandleTracksSortedSearch {
        private var sortState: SortingState = SortingState.ByTime()
        protected var fetching: Job? = null

        override fun handle(
            sortingState: SortingState,
            scope: CoroutineScope,
            query: String,
            playlistId: Int
        ) {
            this.sortState = sortingState
            fetch(scope, sortingState,query,playlistId)
        }

        override fun handle(
            scope: CoroutineScope,
            query: String,
            playlistId: Int
        ) = fetch(scope,sortState,query,playlistId)

       protected abstract fun fetch(
           scope: CoroutineScope,
           sortingState: SortingState,
           query: String,
           playlistId: Int
       )
    }


    abstract class AbstractFetch<T>(
        private val dispatchersList: DispatchersList,
        private val repository: CachedTracksRepository<T>,
        private val handlerFavoritesListFromCache: HandleFavoritesListFromCache<T>,
    ): Abstract(),HandleTracksSortedSearch {

        override fun fetch(
            scope: CoroutineScope,
            sortingState: SortingState,
            query: String,
            playlistId: Int
        ) {
            fetching?.cancel()
            fetching = scope.launch(dispatchersList.io()) {
                repository.fetch(sortingState.copyObj(query),playlistId).collect{
                    handlerFavoritesListFromCache.handle(it)
                }
            }
        }


    }
}

interface HandleFavoritesTracksSortedSearch: HandleTracksSortedSearch {

    class Base @Inject constructor(
        dispatchersList: DispatchersList,
        repository: CachedTracksRepository<MediaItem>,
        handlerFavoritesListFromCache: HandleFavoritesTracksFromCache
    ) :HandleFavoritesTracksSortedSearch,HandleTracksSortedSearch.AbstractFetch<MediaItem>(
        dispatchersList, repository, handlerFavoritesListFromCache
    )

}

//interface HandleSelectedTracksSortedSearch: HandleTracksSortedSearch {
//
//    class Base @Inject constructor(
//        dispatchersList: DispatchersList,
//        repository: CachedTracksRepository,
//        handlerFavoritesListFromCache: HandleFavoritesListFromCacheSelected
//    ) :HandleSelectedTracksSortedSearch,HandleTracksSortedSearch.AbstractFetch(
//        dispatchersList, repository, handlerFavoritesListFromCache
//    )
//
//}

