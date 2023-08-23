//package com.example.musicapp.playlist.presentation
//
//import androidx.lifecycle.LifecycleOwner
//import androidx.media3.common.MediaItem
//import com.example.musicapp.app.core.TracksResultToUiEventCommunicationMapper
//import com.example.musicapp.favorites.presentation.FavoritesViewModel
//import com.example.musicapp.favorites.presentation.FavoritesViewModelTest
//import com.example.musicapp.favorites.presentation.TracksResult
//import com.example.musicapp.favorites.presentation.TracksResultToFavoriteTracksCommunicationMapper
//import com.example.musicapp.favorites.testcore.TestDispatcherList
//import com.example.musicapp.favorites.testcore.TestFavoriteRepository
//import com.example.musicapp.favorites.testcore.TestSingleUiStateCommunication
//import com.example.musicapp.favorites.testcore.TestTemporaryTracksCache
//import com.example.musicapp.main.presentation.TestPlayerCommunication
//import com.example.musicapp.playlist.domain.PlaylistInteractor
//import com.example.musicapp.trending.data.ObjectCreator
//import com.example.musicapp.trending.presentation.TracksUiState
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.FlowCollector
//import org.junit.Assert.assertEquals
//import org.junit.Before
//import org.junit.Test
//
///**
// * Created by HP on 24.05.2023.
// **/
//class PlaylistViewModelTest: ObjectCreator() {
//
//    lateinit var playerCommunication: TestPlayerCommunication
//    lateinit var viewModel: PlaylistViewModel
//    lateinit var repository: TestFavoriteRepository
//    private lateinit var temporaryTracksCache: TestTemporaryTracksCache
//    lateinit var favoriteTracksCommunion: FavoritesViewModelTest.TestFavoritesTracksCommunication
//    lateinit var singleUiStateCommunication: TestSingleUiStateCommunication
//    lateinit var communication: TestPlaylistCommunication
//    lateinit var interactor: TestPlaylistInteractor
//
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Before
//    fun setup() {
//        interactor = TestPlaylistInteractor()
//        playerCommunication = TestPlayerCommunication()
//        communication = TestPlaylistCommunication()
//        val dispatchersList = TestDispatcherList()
//        repository = TestFavoriteRepository()
//        temporaryTracksCache = TestTemporaryTracksCache()
//        singleUiStateCommunication = TestSingleUiStateCommunication()
//        viewModel = PlaylistViewModel(
//            playlistInteractor = interactor,
//            dispatchersList = dispatchersList,
//            dataMapper = TracksResultToPlaylistTracksCommunicationMapper.Base(communication),
//            additionalPlaylistInfo = AdditionalPlaylistInfoCommunication.Base(),
//            playlistCommunication = communication,
//            playerCommunication = playerCommunication,
//            temporaryTracksCache = temporaryTracksCache,
//            tracksRepository = repository,
//            mapper = TracksResultToUiEventCommunicationMapper.Base(
//                singleUiStateCommunication,
//            )
//        )
//
//    }
//
//    @Test
//    fun `test init success`() {
//
//        assertEquals(Triple("1","1","1"),communication.additionalInfo)
//        assertEquals(listOf(getMediaItem()),communication.tracks)
//        assertEquals(TracksUiState.Loading,communication.stateList[0])
//        assertEquals(TracksUiState.Success,communication.stateList[1])
//    }
//
//    @Test
//    fun `test error`() {
//
//        val errorMessage = "no internet"
//        interactor.error = errorMessage
//        viewModel.loadData()
//
//        assertEquals(TracksUiState.Loading,communication.stateList[2])
//        assertEquals(TracksUiState.Error(errorMessage),communication.stateList[3])
//    }
//
//
//    class TestPlaylistCommunication : PlaylistCommunication, ObjectCreator() {
//        val stateList = mutableListOf<TracksUiState>()
//        val tracks = mutableListOf<MediaItem>()
//        var additionalInfo = Triple("", "", "")
//
//        override fun showAdditionalPlaylistInfo(data: Triple<String, String, String>) {
//            additionalInfo = data
//        }
//
//        override fun showUiState(state: TracksUiState) {
//            stateList.add(state)
//        }
//
//            override fun showTracks(tracks: List<MediaItem>) {
//                this.tracks.clear()
//                this.tracks.addAll(tracks)
//            }
//
//            override suspend fun collectState(
//                owner: LifecycleOwner,
//                collector: FlowCollector<TracksUiState>,
//            ) = Unit
//
//            override suspend fun collectTracks(
//                owner: LifecycleOwner,
//                collector: FlowCollector<List<MediaItem>>,
//            ) = Unit
//
//            override suspend fun collectAdditionalPlaylistInfo(
//                owner: LifecycleOwner,
//                collector: FlowCollector<Triple<String, String, String>>,
//            ) = Unit
//
//    }
//
//    class TestPlaylistInteractor: PlaylistInteractor,ObjectCreator(){
//        var error = ""
//
//        override suspend fun fetchData(): TracksResult {
//            return if (error.isEmpty())
//                TracksResult.SuccessAlbumTracks(listOf(getMediaItem()),"1","1","1")
//            else TracksResult.Failure(error)
//        }
//    }
//
//}