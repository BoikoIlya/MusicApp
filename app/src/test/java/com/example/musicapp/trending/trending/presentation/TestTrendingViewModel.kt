package com.example.musicapp.trending.trending.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.main.presentation.PlayerCommunication
import com.example.musicapp.app.main.presentation.PlayerCommunicationState
import com.example.musicapp.trending.domain.PlaylistDomain
import com.example.musicapp.trending.domain.TrackDomain
import com.example.musicapp.trending.domain.TrendingInteractor
import com.example.musicapp.trending.main.presentation.TestPlayerCommunication
import com.example.musicapp.trending.presentation.*
import com.example.musicapp.trending.trending.data.ObjectCreator
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 28.01.2023.
 **/
class TestTrendingViewModel: ObjectCreator() {

    lateinit var viewModel: TrendingViewModel
    lateinit var communication: TestTrendingCommunication
    lateinit var interactor: TestInteractor
    lateinit var playerCommunication: TestPlayerCommunication

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup(){
        val dispatchersList = TestDispatcherList()
        communication = TestTrendingCommunication()
        interactor = TestInteractor()
        playerCommunication = TestPlayerCommunication()
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
            playerCommunication = playerCommunication
        )
    }

    @Test
    fun `test init success and error`() {

        assertEquals(TrendingUiState.Loading,communication.stateList[0])
        assertEquals(TrendingUiState.Success, communication.stateList[1])
        assertEquals( 1, communication.playlistCommunicationCallCount)
        assertEquals( 1, communication.trackCommunicationCallCount)

        val expected = TrendingResult.Error("")
        interactor.result = expected
        viewModel.loadData()

        assertEquals(TrendingUiState.Loading,communication.stateList[2])
        assertEquals(TrendingUiState.Error(""), communication.stateList[3])
        assertEquals( 1, communication.playlistCommunicationCallCount)
        assertEquals( 1, communication.trackCommunicationCallCount)

    }

    @Test
    fun `test play music`() {
        viewModel.playMusic(getMediaItem(), 1)

        assertEquals(PlayerCommunicationState.Play(getMediaItem(),1), playerCommunication.stateList[0])


        interactor.isNewQuerry = true
        viewModel.playMusic(getMediaItem(), 1)

        assertEquals(PlayerCommunicationState.SetQuery(listOf(getMediaItem())), playerCommunication.stateList[1])
        assertEquals(PlayerCommunicationState.Play(getMediaItem(),1), playerCommunication.stateList[2])
    }

    @Test
    fun `test set query`() {
        viewModel.setQuery(listOf(getMediaItem()))

        assertEquals(PlayerCommunicationState.SetQuery(listOf(getMediaItem())), playerCommunication.stateList[0])
    }

    @ExperimentalCoroutinesApi
    class TestDispatcherList(
        private val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()
    ): DispatchersList {
        override fun io(): CoroutineDispatcher = dispatcher

        override fun ui(): CoroutineDispatcher = dispatcher

    }

    class TestTrendingCommunication: TrendingCommunication {
        val stateList = mutableListOf<TrendingUiState>()
        var trackCommunicationCallCount = 0
        var playlistCommunicationCallCount = 0
        override fun showUiState(state: TrendingUiState) {
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
            collector: FlowCollector<TrendingUiState>,
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

         var result: TrendingResult = TrendingResult.Success(Pair(playlists, tracks))
        override suspend fun fetchData(): TrendingResult = result
        override suspend fun checkForNewQuery(): List<MediaItem>  {
            return if(isNewQuerry) listOf(getMediaItem())
            else emptyList()
        }

    }
}