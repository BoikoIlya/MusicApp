package com.example.musicapp.favorites.testcore

import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.FavoritesRepository
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.track_id
import com.example.musicapp.favorites.data.FavoritesTracksRepository
import com.example.musicapp.favorites.data.SortingState
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.trending.domain.TrackDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by HP on 23.05.2023.
 **/
class TestFavoriteRepository: FavoritesTracksRepository {
    val states = emptyList<SortingState>().toMutableList()
    val list = emptyList<MediaItem>().toMutableList()
    var dublicate = false



    override fun fetchData(state: SortingState): Flow<TracksResult> {
        return flow {
            states.add(state)
            when (state) {
                is SortingState.ByArtist -> {
                    list.clear()
                    list.addAll(list.sortedBy {  it.mediaMetadata.artist.toString()  })
                }
                is SortingState.ByName -> list.sortedBy {  it.mediaMetadata.artist.toString()  }
                is SortingState.ByTime -> list.sortedBy {  it.mediaMetadata.artist.toString()  }
                else -> list
            }
            emit(TracksResult.Success(list))
        }

    }

    override suspend fun updateData() {
        TODO("Not yet implemented")
    }

    override suspend fun containsInDb(data: Pair<String, String>): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun addToFavoritesIfNotDuplicated(data: TrackDomain): Int {
        TODO("Not yet implemented")
    }

    override suspend fun addToFavorites(data: TrackDomain): Int {
        TODO("Not yet implemented")
    }

    override suspend fun userId(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun deleteData(id: Int) {
        list.removeIf { it.mediaMetadata.extras?.getInt(track_id)==id }
    }



}