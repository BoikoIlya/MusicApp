package com.example.musicapp.favorites.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.app.core.TracksResultToTracksCommunicationMapper
import com.example.musicapp.app.core.TracksResultToUiEventCommunicationMapper
import com.example.musicapp.favorites.testcore.TestTemporaryTracksCache
import com.example.musicapp.favorites.testcore.TestDispatcherList
import com.example.musicapp.favorites.testcore.TestSingleUiStateCommunication
import com.example.musicapp.favorites.data.FavoriteTracksRepository
import com.example.musicapp.favorites.data.SortingState
import com.example.musicapp.favorites.testcore.TestFavoriteRepository
import com.example.musicapp.main.presentation.TestPlayerCommunication
import com.example.musicapp.trending.data.ObjectCreator
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotSame
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 20.03.2023.
 **/
class FavoritesViewModelTest: ObjectCreator() {

    lateinit var viewModel: FavoritesViewModel
    lateinit var repository: TestFavoriteRepository
    lateinit var playerCommunication: TestPlayerCommunication
    lateinit var temporaryTracksCache: TestTemporaryTracksCache
    lateinit var favoriteTracksCommunion: TestFavoritesTracksCommunication
    lateinit var singleUiStateCommunication: TestSingleUiStateCommunication

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup(){
        val dispatchersList = TestDispatcherList()
        playerCommunication = TestPlayerCommunication()
        repository = TestFavoriteRepository()
        temporaryTracksCache = TestTemporaryTracksCache()
        favoriteTracksCommunion = TestFavoritesTracksCommunication()
        singleUiStateCommunication = TestSingleUiStateCommunication()
        viewModel = FavoritesViewModel(
           repository = repository,
           playerCommunication = playerCommunication,
           temporaryTracksCache = temporaryTracksCache,
            dispatchersList = dispatchersList,
            favoriteTracksRepository = repository,
            tracksResultToTracksCommunicationMapper =  TracksResultToFavoriteTracksCommunicationMapper.Base(favoriteTracksCommunion),
            tracksResultToUiEventCommunicationMapper =  TracksResultToUiEventCommunicationMapper.Base(singleUiStateCommunication),
            favoritesTracksCommunication = favoriteTracksCommunion
        )
    }


    @Test
   fun `test fetch data`(){
        assertEquals(repository.list,favoriteTracksCommunion.dataList)
        assertEquals(SortingState.ByTime(""), repository.states[0])

        viewModel.saveQuery("query")

        viewModel.fetchData(SortingState.ByTime())

        assertEquals(repository.list,favoriteTracksCommunion.dataList)
        assertEquals(SortingState.ByTime("query"), repository.states[1])

        viewModel.fetchData(SortingState.ByName())

        assertEquals(repository.list,favoriteTracksCommunion.dataList)
        assertEquals(SortingState.ByName("query"), repository.states[2])

        viewModel.fetchData(SortingState.ByArtist())

        assertEquals(repository.list,favoriteTracksCommunion.dataList)
        assertEquals(SortingState.ByArtist("query"), repository.states[3])
   }

    @Test
    fun `test remove item`() = runBlocking {
        repository.list.add(getMediaItem())

        viewModel.removeItem("1")

        assertEquals(0,repository.list.size)
        assertEquals(SingleUiEventState.ShowSnackBar.Success::class,singleUiStateCommunication.stateList[0]::class)
    }

    @Test //It is normal to pass not all times because it can not to shuffle list of 2 elements
    fun `test shuffle`() = runBlocking {
        val list = listOf(MediaItem.Builder().setMediaId("2").build(), getMediaItem())
        temporaryTracksCache.tracks.addAll(list)

        viewModel.shuffle()

        assertNotSame(list, favoriteTracksCommunion.dataList)
        assertEquals(list[0].mediaId, favoriteTracksCommunion.dataList[1].mediaId)
    }





    class TestFavoritesTracksCommunication: FavoritesCommunication{

        val states = emptyList<FavoriteTracksUiState>().toMutableList()
        val dataList = emptyList<MediaItem>().toMutableList()

        override fun showUiState(state: FavoriteTracksUiState) {
            states.add(state)
        }

        override fun showTracks(tracks: List<MediaItem>) {
            dataList.clear()
            dataList.addAll(tracks)
        }

        override suspend fun collectState(
            owner: LifecycleOwner,
            collector: FlowCollector<FavoriteTracksUiState>,
        ) = Unit

        override suspend fun collectTracks(
            owner: LifecycleOwner,
            collector: FlowCollector<List<MediaItem>>,
        ) = Unit

    }


}