package com.example.musicapp.favorites.testcore

import androidx.media3.common.MediaItem
import com.example.musicapp.favorites.data.FavoriteTracksRepository
import com.example.musicapp.favorites.data.SortingState
import com.example.musicapp.favorites.presentation.TracksResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by HP on 23.05.2023.
 **/
class TestFavoriteRepository: FavoriteTracksRepository {
    val states = emptyList<SortingState>().toMutableList()
    val list = emptyList<MediaItem>().toMutableList()
    var dublicate = false

    override suspend fun removeTrack(id: String): TracksResult {
        list.removeIf { it.mediaId==id }
        return TracksResult.Success(message = "fddfdfdf")
    }

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

    override suspend fun checkInsertData(data: MediaItem): TracksResult {
        return if(dublicate) TracksResult.Duplicate
        else TracksResult.Success(message = "")
    }

    override suspend fun insertData(data: MediaItem): TracksResult {
        list.add(data)
        return TracksResult.Success()
    }

    override suspend fun contains(id: String): Boolean= list.find { it.mediaId == id } != null

}