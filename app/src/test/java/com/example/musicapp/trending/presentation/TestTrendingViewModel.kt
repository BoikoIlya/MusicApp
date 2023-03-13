package com.example.musicapp.trending.presentation

import androidx.lifecycle.LifecycleOwner
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.trending.domain.PlaylistDomain
import com.example.musicapp.trending.domain.TrackDomain
import com.example.musicapp.trending.domain.TrendingInteractor
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
class TestTrendingViewModel {

    lateinit var viewModel: TrendingViewModel
    lateinit var communication: TestTrendingCommunication
    lateinit var interactor: TestInteractor

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup(){
        val dispatchersList = TestDispatcherList()
        communication = TestTrendingCommunication()
        interactor = TestInteractor()

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
            handleTrendingResult = handleTrendingResult
        )
    }

    @Test
    fun `test loadData success and error`() {

        assertEquals( TrendingUiState.Loading,communication.stateList[0])
        assertEquals( TrendingUiState.Success, communication.stateList[1])
        assertEquals( 1, communication.playlistCommunicationCallCount)
        assertEquals( 1, communication.trackCommunicationCallCount)

        val expected =  TrendingResult.Error("")
        interactor.result = expected
        viewModel.loadData()

        assertEquals( TrendingUiState.Loading,communication.stateList[2])
        assertEquals(TrendingUiState.Error(""), communication.stateList[3])
        assertEquals( 1, communication.playlistCommunicationCallCount)
        assertEquals( 1, communication.trackCommunicationCallCount)

    }


    @ExperimentalCoroutinesApi
    class TestDispatcherList(
        private val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()
    ): DispatchersList {
        override fun io(): CoroutineDispatcher = dispatcher

        override fun ui(): CoroutineDispatcher = dispatcher

    }

    class TestTrendingCommunication: TrendingCommunication{
        val stateList = mutableListOf<TrendingUiState>()
        var trackCommunicationCallCount = 0
        var playlistCommunicationCallCount = 0
        override fun showUiState(state: TrendingUiState) {
            stateList.add(state)
        }

        override fun showTracks(tracks: List<TrackUi>) {
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
            collector: FlowCollector<List<TrackUi>>,
        ) = Unit
    }

    class TestInteractor: TrendingInteractor{
         var result: TrendingResult = TrendingResult.Success(
             Pair(listOf(
                 PlaylistDomain(id = "", name = "", descriptions = "", imgUrl = "", tracksUrl = "")
             ), listOf(
                 TrackDomain(
                     id = "",
                     imageUrl = "",
                     name = "",
                     artistName = "",
                     previewURL = "",
                     albumName = ""
                 )
             )))
        override suspend fun fetchData(): TrendingResult = result

    }
}