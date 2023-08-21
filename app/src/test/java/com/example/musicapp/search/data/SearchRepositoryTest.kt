package com.example.musicapp.search.data

import androidx.media3.common.MediaItem
import androidx.paging.PagingSource
import com.example.musicapp.app.core.HandleError
import com.example.musicapp.app.core.HandleResponse
import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.captcha.data.cache.CaptchaDataStore
import com.example.musicapp.favorites.domain.FavoritesTracksInteractorTest
import com.example.musicapp.favorites.testcore.TestAuthRepository
import com.example.musicapp.favorites.testcore.TestManagerResource
import com.example.musicapp.favorites.testcore.TestTemporaryTracksCache
import com.example.musicapp.main.data.AuthorizationRepositoryTest
import com.example.musicapp.search.data.cloud.SearchTracksService
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

    private lateinit var repository: SearchRepository
    @Mock
    private lateinit var service: SearchTracksService
    private lateinit var tokenStore: AuthorizationRepositoryTest.TestTokenStore
    private lateinit var cache: TestTemporaryTracksCache
    private lateinit var authorizationRepositoryTest: TestAuthRepository
    private lateinit var managerResource: TestManagerResource
    private lateinit var paggingSource: SearchPagingSource
    private lateinit var captchaDataStore: CaptchaDataStore
    private lateinit var captchaRepository: FavoritesTracksInteractorTest.TestCaptchaRepository

    @Before
    fun setup() = runBlocking{
        captchaRepository = FavoritesTracksInteractorTest.TestCaptchaRepository()
        MockitoAnnotations.openMocks(this)
        captchaDataStore = CaptchaDataStore.Base()
        managerResource = TestManagerResource()
        authorizationRepositoryTest = TestAuthRepository()
        cache = TestTemporaryTracksCache()
        tokenStore = AuthorizationRepositoryTest.TestTokenStore()

        service = mock(SearchTracksService::class.java)
        repository = SearchRepository.Base(
            service =service,
            mapper = TrackItem.Mapper.CloudTrackToMediaItemMapper(),
            tokenStore = tokenStore,
            cachedTracks = cache,
            handleResponse = HandleResponse.Base(authorizationRepositoryTest,captchaRepository,HandleError.Base(managerResource)),
            transfer = SearchQueryTransfer.Base(),
            captchaDataStore =captchaDataStore,

        )
        paggingSource = SearchPagingSource(
            service = service,
            query = "",
            mapper = TrackItem.Mapper.CloudTrackToMediaItemMapper(),
            tokenStore = AuthorizationRepositoryTest.TestTokenStore(),
            handleResponse = HandleResponse.Base(authorizationRepositoryTest,captchaRepository,HandleError.Base(managerResource)),
            cachedTracks = cache,
            captchaDataStore =captchaDataStore,

        )
    }


    @Test
    fun `test search tracks by name error`() = runTest {

        val message = "message"
        val error = RuntimeException(message, Throwable())
        given(service.search(any(),any(),any(),any(),any(),any(),any())).willThrow(error)
        managerResource.valueString = message
        repository.search("")

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
        given(service.search(any(),any(),any(),any(),any(),any(), any())).willReturn(getTracksCloud())
        val expectedResult = PagingSource.LoadResult.Page(
            data = getTracksCloud().response.items.map{ it.map(TrackItem.Mapper.CloudTrackToMediaItemMapper())},
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