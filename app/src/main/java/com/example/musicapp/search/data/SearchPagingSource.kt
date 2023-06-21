package com.example.musicapp.search.data

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.musicapp.app.SpotifyDto.SearchTracks
import com.example.musicapp.app.core.HandleResponse
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.data.cache.AccountDataStore
import com.example.musicapp.search.data.cloud.SearchTrackService
import java.lang.Exception

/**
 * Created by HP on 29.04.2023.
 **/
class SearchPagingSource(
    private val service: SearchTrackService,
    private val query: String,
    private val mapper: SearchTracks.Mapper<List<MediaItem>>,
    private val tokenStore: AccountDataStore,
    private val handleResponse: HandleResponse<LoadResult<Int, MediaItem>>,
    private val cachedTracks: TemporaryTracksCache
): PagingSource<Int, MediaItem>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaItem> =
        handleResponse.handle({

            val position = params.key ?: 1

                val cloudData = service.searchTrack(
                    auth = tokenStore.token(),
                    query = query,
                    limit = params.loadSize,
                    offset = position
                )
            Log.d("tag", "load: REQUEST $position")
            if(cloudData.tracks.isEmpty()) throw NoSuchElementException()

                val mediaItems = cloudData.tracks.map(mapper)
                if(mediaItems.isEmpty()){
                    Log.d("tag", "load: EMPTY")
                   return@handle load(LoadParams.Append(params.key!!.plus(1),params.loadSize,false))
                    }

            cachedTracks.addPagingData(mediaItems, position==1)
            Log.d("tag", "load: PAGING LOAD")


            val nextKey = if (mediaItems.size < params.loadSize) null else position + 1
            val prevKey = if (position == 1) null else position - 1

           LoadResult.Page(mediaItems, prevKey, nextKey)

    }, {errorMessage,exception->
                LoadResult.Error(Exception(errorMessage,exception))
            })

    override fun getRefreshKey(state: PagingState<Int, MediaItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}

