package com.example.musicapp.favorites.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.core.testcore.TestTemporaryTracksCache
import com.example.musicapp.core.testcore.TestDispatcherList
import com.example.musicapp.core.testcore.TestSingleUiStateCommunication
import com.example.musicapp.favorites.data.FavoriteTracksRepository
import com.example.musicapp.favorites.data.SortingState
import com.example.musicapp.main.presentation.TestPlayerCommunication
import com.example.musicapp.main.presentation.UiEventsCommunication
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
            favoritesTracksCommunication = favoriteTracksCommunion,
            tracksResultToTracksCommunicationMapper =  TracksResultToTracksCommunicationMapper.Base(favoriteTracksCommunion),
            tracksResultToSingleUiEventCommunicationMapper =  TracksResultToUiEventCommunicationMapper.Base(singleUiStateCommunication, UiEventsCommunication.Base()),
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

    @Test
    fun `test shuffle`() = runBlocking {
        val list = listOf(MediaItem.Builder().setMediaId("2").build(), getMediaItem())
        temporaryTracksCache.tracks.addAll(list)

        viewModel.shuffle()

        assertNotSame(list, favoriteTracksCommunion.dataList)
        assertEquals(list[0].mediaId, favoriteTracksCommunion.dataList[1].mediaId)
    }



    class TestFavoriteRepository: FavoriteTracksRepository {
        val states = emptyList<SortingState>().toMutableList()
        val list = emptyList<MediaItem>().toMutableList()
        var dublicate = false

        override suspend fun removeTrack(id: String): TracksResult {
            list.removeIf { it.mediaId==id }
            return TracksResult.Success(message = "fddfdfdf")
        }

        override fun fetchData(state: SortingState): Flow<TracksResult> {
          return flow {
              states.add(state)
              when (state) {
                  is SortingState.ByArtist -> {
                      list.clear()
                      list.addAll(list.sortedBy {  it.mediaMetadata.artist.toString()  })
                  }
                  is SortingState.ByName -> list.sortedBy {  it.mediaMetadata.artist.toString()  }
                  is SortingState.ByTime -> list.sortedBy {  it.mediaMetadata.artist.toString()  }
                  else -> list
              }
              emit(TracksResult.Success(list))
          }

        }

        override suspend fun checkInsertData(data: MediaItem): TracksResult {
           return if(dublicate) TracksResult.Duplicate
            else TracksResult.Success(message = "")
        }

        override suspend fun insertData(data: MediaItem): TracksResult {
            list.add(data)
            return TracksResult.Success()
        }

        override suspend fun contains(id: String): Boolean= list.find { it.mediaId == id } != null

    }

    class TestFavoritesTracksCommunication: TracksCommunication{

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