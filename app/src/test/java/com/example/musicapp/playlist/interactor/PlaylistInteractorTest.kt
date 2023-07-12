//package com.example.musicapp.playlist.interactor
//
//import com.example.musicapp.app.core.HandleError
//import com.example.musicapp.app.core.HandleResponse
//import com.example.musicapp.favorites.data.FavoritesReposotoryTest
//import com.example.musicapp.favorites.presentation.TracksResult
//import com.example.musicapp.favorites.testcore.TestAuthRepo
//import com.example.musicapp.playlist.data.PlaylistDataDomain
//import com.example.musicapp.playlist.data.PlaylistRepository
//import com.example.musicapp.playlist.domain.PlaylistInteractor
//import com.example.musicapp.trending.data.ObjectCreator
//import com.example.musicapp.trending.domain.TestTrendingInteractor
//import com.example.musicapp.trending.domain.TrackDomain
//import com.example.musicapp.trending.presentation.TrendingResult
//import kotlinx.coroutines.runBlocking
//import org.junit.Assert.assertEquals
//import org.junit.Before
//import org.junit.Test
//import java.net.UnknownHostException
//
///**
// * Created by HP on 23.05.2023.
// **/
//class PlaylistInteractorTest: ObjectCreator() {
//
//    lateinit var interactor: PlaylistInteractor
//    lateinit var auth: TestAuthRepo
//    lateinit var managerResource: TestTrendingInteractor.TestManagerResource
//    lateinit var repository: TestPlaylistRepository
//
//    @Before
//    fun setup(){
//        managerResource = TestTrendingInteractor.TestManagerResource()
//        auth = TestAuthRepo()
//        repository = TestPlaylistRepository()
//        interactor = PlaylistInteractor.Base(
//            handleResponse = HandleResponse.Base(auth, HandleError.Base(managerResource)),
//            repository =repository,
//            mapper = PlaylistDataDomain.ToTrackResultMapper(TrackDomain.ToTrackUiMapper())
//        )
//    }
//
//    @Test
//    fun `test interactor success`() = runBlocking {
//
//       val actual =  interactor.fetchData()
//
//        assertEquals(TracksResult.SuccessAlbumTracks::class,actual::class)
//    }
//
//    @Test
//    fun `test interactor error`() = runBlocking {
//
//        val message = "error"
//        managerResource.valueString = message
//        repository.error = true
//        val actual =  interactor.fetchData()
//        assertEquals(true,actual.map(FavoritesReposotoryTest.TestTracksResultMapper(emptyList(),message)))
//    }
//
//    class TestPlaylistRepository: PlaylistRepository, ObjectCreator(){
//        var error = false
//
//        override suspend fun fetchPlaylists(): PlaylistDataDomain {
//                return if(!error) getPlaylistDataDomain() else throw UnknownHostException()
//        }
//
//    }
//}