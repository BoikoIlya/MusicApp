package com.example.musicapp.trending.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.app.core.TracksResultToUiEventCommunicationMapper
import com.example.musicapp.favorites.testcore.TestTemporaryTracksCache
import com.example.musicapp.favorites.testcore.TestDispatcherList
import com.example.musicapp.favorites.testcore.TestFavoriteRepository
import com.example.musicapp.favorites.testcore.TestSingleUiStateCommunication
import com.example.musicapp.favorites.presentation.FavoritesViewModelTest
import com.example.musicapp.main.presentation.*
import com.example.musicapp.trending.domain.PlaylistDomain
import com.example.musicapp.trending.domain.TrackDomain
import com.example.musicapp.trending.domain.TrendingInteractor
import com.example.musicapp.trending.presentation.*
import com.example.musicapp.trending.data.ObjectCreator
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 28.01.2023.
 **/
class TestTrendingViewModel: ObjectCreator() {

    lateinit var viewModel: TrendingViewModel
    lateinit var communication: TestTrendingCommunication
    lateinit var interactor: TestInteractor
    lateinit var playerCommunication: PlayerCommunication

    lateinit var playerControlsCommunication: TestPlayerControlsCommunication
    lateinit var currentQueueCommunication: TestCurrentQueueCommunication
    lateinit var selectedTrackCommunication: TestSelectedTrackCommunication
    lateinit var mediaController: TestMediaController
    lateinit var singleUiStateCommunication: TestSingleUiStateCommunication
    lateinit var favoriteTracksRepository: TestFavoriteRepository
    lateinit var tracksCache: TestTemporaryTracksCache

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup(){
        val dispatchersList = TestDispatcherList()
        communication = TestTrendingCommunication()
        favoriteTracksRepository = TestFavoriteRepository()
        interactor = TestInteractor()
        playerControlsCommunication = TestPlayerControlsCommunication()
        currentQueueCommunication = TestCurrentQueueCommunication()
        selectedTrackCommunication = TestSelectedTrackCommunication()
        mediaController = TestMediaController()
        singleUiStateCommunication = TestSingleUiStateCommunication()
        tracksCache = TestTemporaryTracksCache()
        playerCommunication = PlayerCommunication.Base(
            playerControlsCommunication =playerControlsCommunication,
            currentQueueCommunication = currentQueueCommunication,
            selectedTrackCommunication =selectedTrackCommunication,
            singleUiEventCommunication = singleUiStateCommunication,
            trackDurationCommunication = TrackDurationCommunication.Base(),
            controller = mediaController,
        )
        val handleTrendingResult = HandleTrendingResult.Base(
            communication,
            dispatchersList,
            TrendingResultMapper(
                communication,
                PlaylistDomain.ToPlaylistUiMapper(),
                TrackDomain.ToTrackUiMapper()
            )
        )
        viewModel = TrendingViewModel(
            interactor = interactor,
            trendingCommunication = communication,
            handleTrendingResult = handleTrendingResult,
            dispatchersList = TestDispatcherList(),
            playerCommunication = playerCommunication,
            tracksRepository = favoriteTracksRepository,
            temporaryTracksCache = tracksCache,
            mapper = TracksResultToUiEventCommunicationMapper.Base(singleUiStateCommunication),

        )
    }

    @Test
    fun `test init success and error`() {

        assertEquals(TracksUiState.Loading,communication.stateList[0])
        assertEquals(TracksUiState.Success, communication.stateList[1])
        assertEquals( 1, communication.playlistCommunicationCallCount)
        assertEquals( 1, communication.trackCommunicationCallCount)

        val expected = TrendingResult.Error("")
        interactor.result = expected
        viewModel.loadData()

        assertEquals(TracksUiState.Loading,communication.stateList[2])
        assertEquals(TracksUiState.Error(""), communication.stateList[3])
        assertEquals( 1, communication.playlistCommunicationCallCount)
        assertEquals( 1, communication.trackCommunicationCallCount)

    }

    @Test
    fun `test play music`() {
        tracksCache.isNewQuery = true
       runBlocking {   tracksCache.saveCurrentPageTracks(listOf(getMediaItem()))}
        viewModel.playMusic(getMediaItem())

        assertEquals(listOf(getMediaItem()), currentQueueCommunication.data)
        assertEquals(listOf(getMediaItem()), mediaController.mediaItemList)
        assertEquals(0, mediaController.seekToDefaultPositionIndex)
        assertEquals(true, mediaController.isPrepared)
        assertEquals(true, mediaController.isPlayingg)
        assertEquals(PlayerControlsState.Play(getMediaItem()),playerControlsCommunication.data)
        assertEquals(getMediaItem(),selectedTrackCommunication.data)

    }

    @Test
    fun `test add tracks to favorites`() {
        viewModel.addTrackToFavorites(getMediaItem("2"))
        assertEquals(1, singleUiStateCommunication.stateList.size)
        assertEquals(SingleUiEventState.ShowDialog::class, singleUiStateCommunication.stateList.last()::class)
    }


   open class TestTrendingCommunication: TrendingCommunication {
        val stateList = mutableListOf<TracksUiState>()
        var trackCommunicationCallCount = 0
        var playlistCommunicationCallCount = 0
        override fun showUiState(state: TracksUiState) {
            stateList.add(state)
        }

        override fun showTracks(tracks: List<MediaItem>) {
           trackCommunicationCallCount++
        }

        override fun showPlayLists(playlists: List<PlaylistUi>) {
            playlistCommunicationCallCount++
        }

        override suspend fun collectState(
            owner: LifecycleOwner,
            collector: FlowCollector<TracksUiState>,
        ) = Unit

        override suspend fun collectPlaylists(
            owner: LifecycleOwner,
            collector: FlowCollector<List<PlaylistUi>>,
        ) = Unit

        override suspend fun collectTracks(
            owner: LifecycleOwner,
            collector: FlowCollector<List<MediaItem>>,
        ) = Unit
    }

    class TestInteractor: TrendingInteractor, ObjectCreator(){
        var isNewQuerry = false
        val tracks = listOf(getTrackDomain())
        val playlists = listOf(getPlaylistDomain())
        var playlistId = ""

         var result: TrendingResult = TrendingResult.Success(Pair(playlists, tracks))
        override suspend fun fetchData(): TrendingResult = result
        override fun savePlaylistId(id: String) {
            playlistId = id
        }

        override suspend fun checkForNewQueue(): List<MediaItem>  {
            return if(isNewQuerry) listOf(getMediaItem())
            else emptyList()
        }

    }
}