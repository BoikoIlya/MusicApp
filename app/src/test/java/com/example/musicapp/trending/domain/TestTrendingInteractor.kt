package com.example.musicapp.trending.domain

import com.example.musicapp.app.core.HandleError
import com.example.musicapp.app.core.HandleResponse
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.ServiceUnavailableException
import com.example.musicapp.favorites.testcore.TestAuthRepo
import com.example.musicapp.favorites.testcore.TestTemporaryTracksCache
import com.example.musicapp.playlist.data.cache.PlaylistIdTransfer
import com.example.musicapp.trending.data.TrendingRepository
import com.example.musicapp.trending.presentation.TrendingResult
import com.example.musicapp.trending.data.ObjectCreator
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

/**
 * Created by HP on 28.01.2023.
 **/
class TestTrendingInteractor: ObjectCreator() {


    lateinit var interactor: TrendingInteractor
    lateinit var repository: TestTrendingReposotory
    lateinit var managerResource: TestManagerResource
    lateinit var cache: TestTemporaryTracksCache
    lateinit var auth: TestAuthRepo

    @Before
    fun setup(){
        managerResource = TestManagerResource()
        cache = TestTemporaryTracksCache()
        auth = TestAuthRepo()
        repository = TestTrendingReposotory(auth)
        interactor = TrendingInteractor.Base(
            repository = repository,
            mapper = TrackDomain.ToTrackUiMapper(),
            tempCache = cache,
            handleUnauthorizedResponse = HandleResponse.Base<TrendingResult>(auth, HandleError.Base(managerResource)),
            transfer = PlaylistIdTransfer.Base()
        )
    }

    @Test
    fun `test fetch data success`() = runBlocking{
        val expected =
            TrendingResult.Success(Pair(listOf(getPlaylistDomain()), listOf(getTrackDomain())))

       val actual = interactor.fetchData()

        assertEquals(expected, actual)
    }

    @Test
    fun `test fetch data no internet error`() = runBlocking{
        val errorMessage = "No internet connection"
        val expected = TrendingResult.Error(errorMessage)
        managerResource.valueString = errorMessage
        repository.exception = ServiceUnavailableException()

        val actual = interactor.fetchData()

        assertEquals(expected, actual)
    }

    @Test
    fun `test fetch data http 401 error`() = runBlocking{
        val expected =
            TrendingResult.Success(Pair(listOf(getPlaylistDomain()), listOf(getTrackDomain())))

        repository.exception = HttpException(Response.error<String>(401, ResponseBody.create("text/plain".toMediaTypeOrNull(), "Unauthorized")))
        val actual = interactor.fetchData()

        assertEquals(expected, actual)
        assertEquals(true, auth.token)
    }



    class TestTrendingReposotory(private val auth: TestAuthRepo): TrendingRepository, ObjectCreator(){

        var exception:Exception? = null

        override suspend fun fetchPlaylists(): List<PlaylistDomain> {
            if(exception!=null && !auth.token) throw exception as Exception
            else return listOf(getPlaylistDomain())
        }

        override suspend fun fetchTracks(): List<TrackDomain>{
            if(exception!=null && !auth.token) throw exception as Exception
            else return listOf(getTrackDomain())
        }

    }

    class TestManagerResource: ManagerResource {
        var valueString = ""
        var valueColor = 0

        override fun getString(id: Int): String = valueString
        override fun getColor(id: Int): Int = valueColor

    }


}