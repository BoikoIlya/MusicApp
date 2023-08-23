package com.kamancho.melisma.favorites.testcore

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.ToMediaItemMapper.Companion.track_id
import com.kamancho.melisma.favorites.data.FavoritesTracksRepository
import com.kamancho.melisma.favorites.data.SortingState
import com.kamancho.melisma.trending.domain.TrackDomain

/**
 * Created by HP on 23.05.2023.
 **/
class TestFavoriteRepository: FavoritesTracksRepository {
    val states = emptyList<SortingState>().toMutableList()
    val list = emptyList<MediaItem>().toMutableList()
    var dublicate = false



//    override fun fetchData(state: SortingState): Flow<TracksResult> {
//        return flow {
//            states.add(state)
//            when (state) {
//                is SortingState.ByArtist -> {
//                    list.clear()
//                    list.addAll(list.sortedBy {  it.mediaMetadata.artist.toString()  })
//                }
//                is SortingState.ByName -> list.sortedBy {  it.mediaMetadata.artist.toString()  }
//                is SortingState.ByTime -> list.sortedBy {  it.mediaMetadata.artist.toString()  }
//                else -> list
//            }
//            emit(TracksResult.Success(list))
//        }
//
//    }

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