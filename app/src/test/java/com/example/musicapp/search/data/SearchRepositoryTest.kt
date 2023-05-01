package com.example.musicapp.search.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.musicapp.app.SpotifyDto.SearchDto
import com.example.musicapp.app.SpotifyDto.SearchItem
import com.example.musicapp.app.SpotifyDto.SearchTracks
import com.example.musicapp.favorites.presentation.FavoritesViewModelTest
import com.example.musicapp.main.data.AuthorizationRepositoryTest
import com.example.musicapp.main.data.cache.TokenStore
import com.example.musicapp.main.di.AppComponent
import com.example.musicapp.main.di.AppModule
import com.example.musicapp.search.data.cloud.SearchTrackService
import com.example.testapp.spotifyDto.Album
import com.example.testapp.spotifyDto.ExternalIds
import com.example.testapp.spotifyDto.ExternalUrls
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by HP on 29.04.2023.
 **/




class SearchRepositoryTest {

    lateinit var repository: SearchRepository
    lateinit var service: TestSearchService
    lateinit var tokenStore: AuthorizationRepositoryTest.TestTokenStore


    @Before
    fun setup(){
        service = TestSearchService()
        tokenStore = AuthorizationRepositoryTest.TestTokenStore()
        repository = SearchRepository.Base(

            service =service,
            mapper = SearchTracks.ToMediaItemMapper(),
            tokenStore = tokenStore
        )
    }


    @Test
    fun `test search tracks by name`() = runBlocking {




    }


    class TestSearchService: SearchTrackService{
        var dto = SearchDto(
            SearchTracks(
                href = "1",
                items = listOf(SearchItem(
                    album = Album(
                        album_type = "1",
                        artists = listOf(),
                        external_urls = ExternalUrls(spotify = ""),
                        href = "1",
                        id = "1",
                        images = listOf(),
                        name = "1",
                        release_date = "1",
                        release_date_precision = "1",
                        total_tracks = 1,
                        type = "1",
                        uri = "1"
                    ),
                    artists = listOf(),
                    available_markets = listOf(),
                    disc_number = 0,
                    duration_ms = 0,
                    explicit = false,
                    external_ids = ExternalIds(isrc = ""),
                    external_urls = ExternalUrls(spotify = ""),
                    href = "1",
                    id = "1",
                    is_local = false,
                    name = "1",
                    popularity = 1,
                    preview_url = "1",
                    track_number = 1,
                    type = "1",
                    uri = "1"
                )),
                limit = 1,
                next = "",
                offset = 0,
                previous =1,
                total = 0
            )
        )
        override suspend fun searchTrack(
            auth: String,
            query: String,
            market: String,
            limit: Int,
            offset: Int,
        ): SearchDto {
            return dto
        }

    }
}