package com.kamancho.melisma.favorites.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.CacheRepository
import com.kamancho.melisma.app.core.ConnectionChecker
import com.kamancho.melisma.app.core.HandleFavoritesTracksSortedSearch
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.app.core.TrackChecker
import com.kamancho.melisma.app.core.TracksResultToUiEventCommunicationMapper
import com.kamancho.melisma.favorites.testcore.TestTemporaryTracksCache
import com.kamancho.melisma.favorites.testcore.TestDispatcherList
import com.kamancho.melisma.favorites.testcore.TestSingleUiStateCommunication
import com.kamancho.melisma.favorites.data.SortingState
import com.kamancho.melisma.favorites.testcore.TestFavoriteRepository
import com.kamancho.melisma.favorites.testcore.TestFavoritesTracksInteractor
import com.kamancho.melisma.favorites.testcore.TestManagerResource
import com.kamancho.melisma.main.presentation.TestPlayerCommunication
import com.kamancho.melisma.player.presentation.PlayingTrackIdCommunication
import com.kamancho.melisma.trending.data.ObjectCreator
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
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
    private lateinit var cachedTracksRepository:TestCacheRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup(){
        cachedTracksRepository = TestCacheRepository()
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
            tracksResultToUiEventCommunicationMapper =  TracksResultToUiEventCommunicationMapper.Base(singleUiStateCommunication,playingTrackIdCommuniaction),
            favoritesTracksCommunication = favoriteTracksCommunion,
            interactor = interactor,
            singleUiEventCommunication = singleUiStateCommunication,
            resetSwipeActionCommunication = resetSwipeActionCommunication,
            handlerFavoritesUiUpdate = HandlerFavoritesTracksUiUpdate.Base(singleUiStateCommunication, favoriteTracksCommunion ,interactor),
            trackChecker = TrackChecker.Base(singleUiStateCommunication,managerResource,connectionChecker),
            cachedTracksRepository = cachedTracksRepository,
            handleTracksSortedSearch = HandleFavoritesTracksSortedSearch.Base(
                dispatchersList = dispatchersList,
                repository =cachedTracksRepository,
                handlerFavoritesListFromCache = HandleFavoritesTracksFromCache.Base(favoriteTracksCommunion)
            ),
            managerResource = managerResource,

        )
    }


    @Test
   fun `test fetch data`(){
        assertEquals(cachedTracksRepository.list ,favoriteTracksCommunion.dataList)
        assertEquals(SortingState.ByTime(""), cachedTracksRepository.states[0])

        viewModel.saveQuery("query")

        viewModel.fetchData(SortingState.ByTime())

        assertEquals(cachedTracksRepository.list,favoriteTracksCommunion.dataList)
        assertEquals(SortingState.ByTime("query"), cachedTracksRepository.states[1])

        viewModel.fetchData(SortingState.ByName())

        assertEquals(cachedTracksRepository.list,favoriteTracksCommunion.dataList)
        assertEquals(SortingState.ByName("query"), cachedTracksRepository.states[2])

        viewModel.fetchData(SortingState.ByArtist())

        assertEquals(cachedTracksRepository.list,favoriteTracksCommunion.dataList)
        assertEquals(SortingState.ByArtist("query"), cachedTracksRepository.states[3])

   }

    @Test
    fun `test init update`(){
        assertEquals(FavoritesUiState.DisableLoading,favoriteTracksCommunion.loading[0])
        assertEquals(FavoritesUiState.Success,favoriteTracksCommunion.states[0])


        cachedTracksRepository.list.clear()
        interactor.updateDataError = "s"
        viewModel.update(false)
        viewModel.fetchData()
        assertEquals(FavoritesUiState.Loading,favoriteTracksCommunion.loading[1])
        assertEquals(FavoritesUiState.DisableLoading,favoriteTracksCommunion.loading[2])
        assertEquals(FavoritesUiState.Failure,favoriteTracksCommunion.states[1])
        assertEquals(SingleUiEventState.ShowSnackBar.Error::class,singleUiStateCommunication.stateList.last()::class)

        viewModel.update(true)
        viewModel.fetchData()
        assertEquals(FavoritesUiState.Loading,favoriteTracksCommunion.loading[3])
        assertEquals(FavoritesUiState.DisableLoading,favoriteTracksCommunion.loading[4])
        assertEquals(FavoritesUiState.Failure,favoriteTracksCommunion.states[2])
    }






    class TestFavoritesTracksCommunication: FavoritesTracksCommunication{

        val states = emptyList<FavoritesUiState>().toMutableList()
        val dataList = emptyList<MediaItem>().toMutableList()
        val loading = emptyList<FavoritesUiState>().toMutableList()
        override fun showLoading(state: FavoritesUiState) {
            loading.add(state)
        }

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

        override suspend fun collectData(
            owner: LifecycleOwner,
            collector: FlowCollector<List<MediaItem>>,
        ) = Unit

        override suspend fun collectLoading(
            owner: LifecycleOwner,
            collector: FlowCollector<FavoritesUiState>,
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

        override suspend fun collectIgnoreLifecycle(collector: FlowCollector<Int>) = Unit

        override suspend fun collect(
            lifecycleOwner: LifecycleOwner,
            collector: FlowCollector<Int>,
        ) = Unit
    }

    class TestCacheRepository: CacheRepository<MediaItem>,ObjectCreator() {
        val list = emptyList<MediaItem>().toMutableList()
        val states = emptyList<SortingState>().toMutableList()
        init {
            list.add(getMediaItem())
        }

        override fun fetch(sortingState: SortingState, playlistId: Int): Flow<List<MediaItem>> {
            states.add(sortingState)
            return flow { emit(list) }
        }

        override suspend fun isDbEmpty(playlistId: Int): Boolean = list.isEmpty()
    }


}