package com.example.musicapp.favorites.testcore

import androidx.media3.common.MediaItem
import com.example.musicapp.favorites.data.SortingState
import com.example.musicapp.favorites.domain.FavoritesTracksInteractor
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.trending.domain.TrackDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by HP on 10.07.2023.
 **/
class TestFavoritesTracksInteractor: FavoritesTracksInteractor {
    val list = emptyList<MediaItem>().toMutableList()
    val states = emptyList<SortingState>().toMutableList()
    var savedItem:MediaItem? = null
    var equalsWithUserId = false
    var containsTrackInDb = false
    var updateDataError = ""
    var isDBEmpty = true
    var state = TracksResult.Success()

    override fun fetchData(sortingState: SortingState): Flow<TracksResult> {
        states.add(sortingState)
        return flow { emit(TracksResult.Success(list = list)) }
    }

    override fun saveDeletingItem(item: MediaItem) {
        savedItem = item
    }

    override suspend fun equalsWithUserId(id: Int): Boolean {
        return equalsWithUserId
    }

    override suspend fun containsTrackInDb(item: Pair<String, String>): Boolean {
        return containsTrackInDb
    }

    override suspend fun updateData(): String {
        return updateDataError
    }

    override suspend fun isDBEmpty(): Boolean {
        return isDBEmpty
    }

    override suspend fun addToFavoritesIfNotDuplicated(item: MediaItem): TracksResult {
        return state
    }

    override suspend fun addToFavorites(item: MediaItem): TracksResult {
       return  state
    }

    override suspend fun deleteData(id: Int): TracksResult {
      return  state
    }
}