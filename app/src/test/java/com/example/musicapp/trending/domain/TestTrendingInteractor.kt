package com.example.musicapp.trending.domain

import com.example.musicapp.app.core.HandleError
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.NoInternetConnectionException
import com.example.musicapp.trending.data.TrendingRepository
import com.example.musicapp.trending.presentation.TrendingResult
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 28.01.2023.
 **/
class TestTrendingInteractor {


    lateinit var interactor: TrendingInteractor
    lateinit var repository: TestTrendingReposotory
    lateinit var managerResource: TestManagerResource

    @Before
    fun setup(){
        managerResource = TestManagerResource()
        repository = TestTrendingReposotory()
        interactor = TrendingInteractor.Base(
            repository = repository,
            handleError = HandleError.Base(managerResource)
        )
    }

    @Test
    fun `test fetch data success`() = runBlocking{
        val expected =
            TrendingResult.Success(Pair(repository.playlistResult, repository.trackResult))

       val actual = interactor.fetchData()

        assertEquals(expected, actual)
    }

    @Test
    fun `test fetch data error`() = runBlocking{
        val errorMessage = "No internet connection"
        val expected = TrendingResult.Error(errorMessage)
        managerResource.value = errorMessage
        repository.throwException = true

        val actual = interactor.fetchData()

        assertEquals(expected, actual)
    }



    class TestTrendingReposotory: TrendingRepository{
        var playlistResult = listOf(PlaylistDomain(
            id = "",
            name = "",
            descriptions = "",
            imgUrl = "",
            tracksUrl = ""
        ))

        var trackResult = listOf(TrackDomain(
            id = "",
            imageUrl = "",
            name = "",
            artistName = "",
            previewURL = "",
            albumName = ""
        ))

        var throwException = false

        override suspend fun fetchPlaylists(): List<PlaylistDomain> {
            if(throwException) throw NoInternetConnectionException()
           return playlistResult
        }

        override suspend fun fetchTracks(): List<TrackDomain>{
            if(throwException) throw NoInternetConnectionException()
            return trackResult
        }

    }

    class TestManagerResource: ManagerResource {
        var value = ""

        override fun getString(id: Int): String = value

    }
}