package com.example.musicapp.search.data

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.musicapp.app.core.HandleResponse
import com.example.musicapp.app.core.Mapper
import com.example.musicapp.app.vkdto.SearchPlaylistsCloud
import com.example.musicapp.app.vkdto.TracksCloud
import com.example.musicapp.captcha.data.cache.CaptchaDataStore
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.data.cache.AccountDataStore
import com.example.musicapp.search.data.cloud.PlaylistsCloudToPlaylistUiMapper
import com.example.musicapp.search.data.cloud.SearchPlaylistsService
import com.example.musicapp.search.data.cloud.SearchTracksService
import com.example.musicapp.search.data.cloud.TracksCloudToMediaItemsMapper
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import java.lang.Exception

/**
 * Created by HP on 29.04.2023.
 **/
abstract class SearchPagingSource<C,M:Any>(
    private val searchCall: suspend (String,String,Int,Int,String,String)->C,
    private val query: String,
    private val mapper: Mapper<C,List<M>>,
    private val tokenStore: AccountDataStore,
    private val handleResponse: HandleResponse,
    private val captchaDataStore: CaptchaDataStore,
    private val additionalAction: suspend (List<M>,Int)->Unit
): PagingSource<Int, M>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, M> =
        handleResponse.handle({

            val position = params.key ?: 0
                val cloudData = searchCall.invoke(
                        tokenStore.token(),
                        query,
                        params.loadSize,
                        position * params.loadSize,
                        captchaDataStore.captchaId(),
                        captchaDataStore.captchaEnteredData(),
                    )



            val items= mapper.map(cloudData) //cloudData.response.items.map { it.map(mapper)}

            if(items.isEmpty() && position==0) throw NoSuchElementException()


            additionalAction.invoke(items,position)


            val nextKey = if (items.size < params.loadSize) null else position + 1
            val prevKey = if (position == 0) null else position - 1

           LoadResult.Page(items, prevKey, nextKey)

    }, {errorMessage,exception->
                LoadResult.Error(Exception(errorMessage,exception))
            })

    override fun getRefreshKey(state: PagingState<Int, M>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}

class MediaItemsSearchPagingSource (
    service: SearchTracksService,
    query: String,
    mapper: TracksCloudToMediaItemsMapper,
    tokenStore: AccountDataStore,
    handleResponse: HandleResponse,
    captchaDataStore: CaptchaDataStore,
    private val cachedTracks: TemporaryTracksCache,
): SearchPagingSource<TracksCloud,MediaItem>(
    searchCall = {token,query,count,offset,captchaId,captchaEnteredData->
        service.search(token,query,count,offset,captchaId,captchaEnteredData)
    },
    query = query,
    mapper = mapper,
    tokenStore = tokenStore,
    handleResponse = handleResponse,
    captchaDataStore = captchaDataStore,
    additionalAction = {mediaItems,position -> cachedTracks.addPagingData(mediaItems, position==0) }
){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaItem> {
        return super.load(params)
    }
}

class PlaylistsSearchPagingSource (
    service: SearchPlaylistsService,
    query: String,
    mapper: PlaylistsCloudToPlaylistUiMapper,
    tokenStore: AccountDataStore,
    handleResponse: HandleResponse,
    captchaDataStore: CaptchaDataStore
): SearchPagingSource<SearchPlaylistsCloud,PlaylistUi>(
    searchCall = {token,query,count,offset,captchaId,captchaEnteredData->
        service.search(token,query,count,offset,captchaId,captchaEnteredData)
    },
    query = query,
    mapper = mapper,
    tokenStore = tokenStore,
    handleResponse = handleResponse,
    captchaDataStore = captchaDataStore,
    additionalAction = {mediaItems,position ->}
)
