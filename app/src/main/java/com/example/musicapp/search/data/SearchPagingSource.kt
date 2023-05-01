package com.example.musicapp.search.data

import androidx.media3.common.MediaItem
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.musicapp.app.SpotifyDto.SearchDto
import com.example.musicapp.app.SpotifyDto.SearchTracks
import com.example.musicapp.app.core.Mapper
import com.example.musicapp.main.data.cache.TokenStore
import com.example.musicapp.search.data.cloud.SearchTrackService
import com.example.musicapp.trending.domain.TrackDomain

/**
 * Created by HP on 29.04.2023.
 **/
class SearchPagingSource(
    private val service: SearchTrackService,
    private val query: String,
    private val mapper: SearchTracks.Mapper<List<MediaItem>>,
    private val tokenStore: TokenStore
): PagingSource<Int, MediaItem>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaItem> {
            val position = params.key ?: 1

            val cloudData = service.searchTrack(
               auth = tokenStore.read(),
               query = query,
                limit = params.loadSize,
                offset = position
            )

            val mediaItems= cloudData.tracks.map(mapper)
            val nextKey = if(mediaItems.size < params.loadSize) null else position+1
            val prevKey = if(position==1) null else position-1

           return LoadResult.Page(mediaItems, prevKey, nextKey)

    }

    override fun getRefreshKey(state: PagingState<Int, MediaItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}