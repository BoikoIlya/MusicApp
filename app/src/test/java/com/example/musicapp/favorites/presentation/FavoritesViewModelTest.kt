package com.example.musicapp.favorites.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.ConnectionChecker
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.app.core.TrackChecker
import com.example.musicapp.app.core.TracksResultToUiEventCommunicationMapper
import com.example.musicapp.favorites.testcore.TestTemporaryTracksCache
import com.example.musicapp.favorites.testcore.TestDispatcherList
import com.example.musicapp.favorites.testcore.TestSingleUiStateCommunication
import com.example.musicapp.favorites.data.SortingState
import com.example.musicapp.favorites.testcore.TestFavoriteRepository
import com.example.musicapp.favorites.testcore.TestFavoritesTracksInteractor
import com.example.musicapp.favorites.testcore.TestManagerResource
import com.example.musicapp.main.presentation.TestPlayerCommunication
import com.example.musicapp.player.presentation.PlayingTrackIdCommunication
import com.example.musicapp.trending.data.ObjectCreator
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 20.03.2023.
 **/
class FavoritesViewModelTest: ObjectCreator() {

    private lateinit var viewModel: FavoritesTracksViewModel
    private lateinit var repository: TestFavoriteRepository
    private lateinit var playerCommunication: TestPlayerCommunication
    private lateinit var temporaryTracksCache: TestTemporaryTracksCache
    private lateinit var favoriteTracksCommunion: TestFavoritesTracksCommunication
    private lateinit var singleUiStateCommunication: TestSingleUiStateCommunication
    private lateinit var interactor: TestFavoritesTracksInteractor
    private lateinit var resetSwipeActionCommunication: TestRestSwipeActionCommuniction
    private lateinit var managerResource: TestManagerResource
    private lateinit var playingTrackIdCommuniaction: TestPlyingTrackIdCommuniacation
    private lateinit var connectionChecker: TestConnectionChecker

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup(){
        connectionChecker = TestConnectionChecker()
        playingTrackIdCommuniaction = TestPlyingTrackIdCommuniacation()
        managerResource = TestManagerResource()
        val dispatchersList = TestDispatcherList()
        playerCommunication = TestPlayerCommunication()
        repository = TestFavoriteRepository()
        temporaryTracksCache = TestTemporaryTracksCache()
        favoriteTracksCommunion = TestFavoritesTracksCommunication()
        singleUiStateCommunication = TestSingleUiStateCommunication()
        interactor = TestFavoritesTracksInteractor()
        resetSwipeActionCommunication = TestRestSwipeActionCommuniction()
        viewModel = FavoritesTracksViewModel(
           playerCommunication = playerCommunication,
           temporaryTracksCache = temporaryTracksCache,
            dispatchersList = dispatchersList,
            tracksResultToTracksCommunicationMapper =  TracksResultToFavoriteTracksCommunicationMapper.Base(favoriteTracksCommunion),
            tracksResultToUiEventCommunicationMapper =  TracksResultToUiEventCommunicationMapper.Base(singleUiStateCommunication,playingTrackIdCommuniaction),
            favoritesTracksCommunication = favoriteTracksCommunion,
            interactor = interactor,
            singleUiEventCommunication = singleUiStateCommunication,
            resetSwipeActionCommunication = resetSwipeActionCommunication,
            handlerFavoritesUiUpdate = HandlerFavoritesTracksUiUpdate.Base(singleUiStateCommunication, favoriteTracksCommunion ,interactor),
            trackChecker = TrackChecker.Base(singleUiStateCommunication,managerResource,connectionChecker),
        )
    }


    @Test
   fun `test fetch data`(){
        assertEquals(interactor.list ,favoriteTracksCommunion.dataList)
        assertEquals(SortingState.ByTime(""), interactor.states[0])

        viewModel.saveQuery("query")

        viewModel.fetchData(SortingState.ByTime())

        assertEquals(interactor.list,favoriteTracksCommunion.dataList)
        assertEquals(SortingState.ByTime("query"), interactor.states[1])

        viewModel.fetchData(SortingState.ByName())

        assertEquals(interactor.list,favoriteTracksCommunion.dataList)
        assertEquals(SortingState.ByName("query"), interactor.states[2])

        viewModel.fetchData(SortingState.ByArtist())

        assertEquals(interactor.list,favoriteTracksCommunion.dataList)
        assertEquals(SortingState.ByArtist("query"), interactor.states[3])

   }

    @Test
    fun `test init update`(){

        assertEquals(FavoritesUiState.Loading,favoriteTracksCommunion.states[0])
        assertEquals(FavoritesUiState.Success,favoriteTracksCommunion.states[1])

        interactor.isDBEmpty = false
        interactor.updateDataError = "s"
        viewModel.update(false)
        assertEquals(FavoritesUiState.Failure,favoriteTracksCommunion.states[2])
        assertEquals(SingleUiEventState.ShowSnackBar.Error::class,singleUiStateCommunication.stateList.last()::class)

        viewModel.update(true)
        assertEquals(FavoritesUiState.Loading,favoriteTracksCommunion.states[4])
        assertEquals(FavoritesUiState.Failure,favoriteTracksCommunion.states[5])
    }






    class TestFavoritesTracksCommunication: FavoritesTracksCommunication{

        val states = emptyList<FavoritesUiState>().toMutableList()
        val dataList = emptyList<MediaItem>().toMutableList()

        override fun showUiState(state: FavoritesUiState) {
            states.add(state)
        }

        override fun showData(tracks: List<MediaItem>) {
            dataList.clear()
            dataList.addAll(tracks)
        }

        override suspend fun collectState(
            owner: LifecycleOwner,
            collector: FlowCollector<FavoritesUiState>,
        ) = Unit

        override suspend fun collectTracks(
            owner: LifecycleOwner,
            collector: FlowCollector<List<MediaItem>>,
        ) = Unit

    }

    class TestRestSwipeActionCommuniction: ResetSwipeActionCommunication{
        var triggerdTimes = 0

        override suspend fun collect(
            lifecycleOwner: LifecycleOwner,
            collector: FlowCollector<Unit>,
        ) = Unit

        override suspend fun map(newValue: Unit) {
            triggerdTimes++
        }

    }

    class TestConnectionChecker: ConnectionChecker{
        var isConnected = true

        override suspend fun isDeviceHaveConnection(): Boolean = isConnected

    }

    class TestPlyingTrackIdCommuniacation: PlayingTrackIdCommunication {
        var value = -1

        override fun map(newValue: Int) {
           value = newValue
        }

        override suspend fun collect(
            lifecycleOwner: LifecycleOwner,
            collector: FlowCollector<Int>,
        ) = Unit
    }
}