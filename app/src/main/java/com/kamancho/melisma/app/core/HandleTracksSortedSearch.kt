package com.kamancho.melisma.app.core

import android.util.Log
import androidx.media3.common.MediaItem
import com.kamancho.melisma.favorites.data.SortingState
import com.kamancho.melisma.favorites.presentation.HandleListFromCache
import com.kamancho.melisma.favorites.presentation.HandleFavoritesTracksFromCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
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
        playlistId: String
    )

    fun handle(
        scope: CoroutineScope,
        query: String,
        playlistId: String
    )

    abstract class Abstract: HandleTracksSortedSearch {
        private var sortState: SortingState = SortingState.ByTime()
        protected var fetching: Job? = null

        override fun handle(
            sortingState: SortingState,
            scope: CoroutineScope,
            query: String,
            playlistId: String
        ) {
            this.sortState = sortingState
            fetch(scope, sortingState,query,playlistId)
        }

        override fun handle(
            scope: CoroutineScope,
            query: String,
            playlistId: String
        ) = fetch(scope,sortState,query,playlistId)

       protected abstract fun fetch(
           scope: CoroutineScope,
           sortingState: SortingState,
           query: String,
           playlistId: String
       )
    }


    abstract class AbstractFetch<T>(
        private val dispatchersList: DispatchersList,
        private val repository: CacheRepository<T>,
        private val handlerFavoritesListFromCache: HandleListFromCache<T>,
    ): Abstract(),HandleTracksSortedSearch {

        override fun fetch(
            scope: CoroutineScope,
            sortingState: SortingState,
            query: String,
            playlistId: String
        ) {
            fetching?.cancel()
            fetching = scope.launch(dispatchersList.io()) {
                Log.d("tag", "result 0: ")
                repository.fetch(sortingState.copyObj(query),playlistId).collectLatest{
                    Log.d("tag", "result 1: ${it.size} ")
                    handlerFavoritesListFromCache.handle(it)
                }
            }
        }

    }
}

interface HandleFavoritesTracksSortedSearch: HandleTracksSortedSearch {

    class Base @Inject constructor(
        dispatchersList: DispatchersList,
        repository: CacheRepository<MediaItem>,
        handlerFavoritesListFromCache: HandleFavoritesTracksFromCache,
    ) :HandleFavoritesTracksSortedSearch,HandleTracksSortedSearch.AbstractFetch<MediaItem>(
        dispatchersList, repository, handlerFavoritesListFromCache
    )

}


