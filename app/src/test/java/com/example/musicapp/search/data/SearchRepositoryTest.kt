package com.example.musicapp.search.data

import androidx.media3.common.MediaItem
import androidx.paging.PagingSource
import com.example.musicapp.app.core.HandleError
import com.example.musicapp.app.core.HandleResponse
import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.favorites.testcore.TestAuthRepository
import com.example.musicapp.favorites.testcore.TestManagerResource
import com.example.musicapp.favorites.testcore.TestTemporaryTracksCache
import com.example.musicapp.main.data.AuthorizationRepositoryTest
import com.example.musicapp.search.data.cloud.SearchTrackService
import com.example.musicapp.searchhistory.data.cache.SearchQueryTransfer
import com.example.musicapp.trending.data.ObjectCreator
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.lang.Exception
import java.lang.RuntimeException

/**
 * Created by HP on 29.04.2023.
 **/

class SearchRepositoryTest: ObjectCreator() {

    lateinit var repository: SearchRepository
    @Mock
    lateinit var service: SearchTrackService
    lateinit var tokenStore: AuthorizationRepositoryTest.TestTokenStore
    lateinit var cache: TestTemporaryTracksCache
    lateinit var authorizationRepositoryTest: TestAuthRepository
    lateinit var managerResource: TestManagerResource
    lateinit var paggingSource: SearchPagingSource

    @Before
    fun setup() = runBlocking{
        MockitoAnnotations.openMocks(this)

        managerResource = TestManagerResource()
        authorizationRepositoryTest = TestAuthRepository()
        cache = TestTemporaryTracksCache()
        tokenStore = AuthorizationRepositoryTest.TestTokenStore()

        service = mock(SearchTrackService::class.java)
        repository = SearchRepository.Base(
            service =service,
            mapper = TrackItem.Mapper.CloudTrackToMediaItemMapper(),
            tokenStore = tokenStore,
            cachedTracks = cache,
            handleResponse = HandleResponse.Base(authorizationRepositoryTest,HandleError.Base(managerResource)),
            transfer = SearchQueryTransfer.Base()
        )
        paggingSource = SearchPagingSource(
            service = service,
            query = "",
            mapper = TrackItem.Mapper.CloudTrackToMediaItemMapper(),
            tokenStore = AuthorizationRepositoryTest.TestTokenStore(),
            handleResponse = HandleResponse.Base(authorizationRepositoryTest,HandleError.Base(managerResource)),
            cachedTracks = cache
        )
    }


    @Test
    fun `test search tracks by name error`() = runTest {

        val message = "message"
        val error = RuntimeException(message, Throwable())
        given(service.searchTrack(any(),any(),any(),any(),any())).willThrow(error)
        managerResource.valueString = message
        repository.searchTracksByName("")

        val expectedResult = PagingSource.LoadResult.Error<Int, MediaItem>(Exception(message))
        assertEquals(
            expectedResult::class, paggingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )::class
        )
    }

    @Test
    fun `test refresh`() = runTest {
        given(service.searchTrack(any(),any(),any(),any(),any())).willReturn(getTracksCloud())
        val expectedResult = PagingSource.LoadResult.Page(
            data = getTracksCloud().handle().map{ it.map(TrackItem.Mapper.CloudTrackToMediaItemMapper())},
            prevKey = null,
            nextKey = 1
        )
        assertEquals(
            expectedResult::class, paggingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )::class
        )
    }


}