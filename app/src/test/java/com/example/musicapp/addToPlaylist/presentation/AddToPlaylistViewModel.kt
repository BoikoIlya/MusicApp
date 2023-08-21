package com.example.musicapp.addToPlaylist.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import com.example.musicapp.addtoplaylist.domain.SelectedTracksInteractor
import com.example.musicapp.addtoplaylist.presentation.AddToPlaylistCommunication
import com.example.musicapp.addtoplaylist.presentation.AddToPlaylistViewModel
import com.example.musicapp.addtoplaylist.presentation.HandleAddToPlaylistTracksUiUpdate
import com.example.musicapp.addtoplaylist.presentation.HandleCachedTracksSelected
import com.example.musicapp.addtoplaylist.presentation.SelectedTrackUi
import com.example.musicapp.creteplaylist.presentation.SelectedTracksCommunication
import com.example.musicapp.favorites.data.SortingState
import com.example.musicapp.favorites.presentation.FavoritesUiState
import com.example.musicapp.favorites.testcore.TestDispatcherList
import com.example.musicapp.favorites.testcore.TestFavoritesTracksInteractor
import com.example.musicapp.favorites.testcore.TestSingleUiStateCommunication
import com.example.musicapp.main.presentation.SelectedTrackCommunication
import com.example.musicapp.trending.data.ObjectCreator
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 01.08.2023.
 **/
class AddToPlaylistViewModelTest: ObjectCreator() {

    private lateinit var viewModel: AddToPlaylistViewModel
    private lateinit var globalSingleUiEventCommunication: TestSingleUiStateCommunication
    private lateinit var communication: TestAddToPlaylistCommunication
    private lateinit var favoritesInteractor: TestFavoritesTracksInteractor
    private lateinit var interactor:TestSelectedTrackInteractor
    private lateinit var selectedTracksCommunication: TestSelectedTracksCommunication

    @Before
    fun setup(){
        globalSingleUiEventCommunication = TestSingleUiStateCommunication()
        selectedTracksCommunication = TestSelectedTracksCommunication()
        interactor = TestSelectedTrackInteractor()
        favoritesInteractor = TestFavoritesTracksInteractor()
        communication = TestAddToPlaylistCommunication()
        viewModel = AddToPlaylistViewModel(
            handlerFavoritesUiUpdate =  HandleAddToPlaylistTracksUiUpdate.Base(
                globalSingleUiEventCommunication,
                communication,
                favoritesInteractor
            ),
            dispatchersList = TestDispatcherList(),
            communication = communication,
            selectedTracksCommunication = selectedTracksCommunication,
            globalSingleUiEventCommunication = globalSingleUiEventCommunication,
            interactor = interactor,
            handleCachedTracksSelected = HandleCachedTracksSelected.Base(communication)
        )
    }

    @Test
    fun `test handleItem`()= runBlocking {
        interactor.saveList(listOf(getSelectedTrackUi()))
        viewModel.handleItemClick(getSelectedTrackUi())
        assertEquals(true,interactor.addItem)
        assertEquals(interactor.list.size,communication.dataList.size)
        assertEquals(interactor.list.size,selectedTracksCommunication.list.size)
    }




    class TestAddToPlaylistCommunication: AddToPlaylistCommunication {

        val states = emptyList<FavoritesUiState>().toMutableList()
        val dataList = emptyList<SelectedTrackUi>().toMutableList()
        val loading = emptyList<FavoritesUiState>().toMutableList()
        override fun showLoading(state: FavoritesUiState) {
            loading.add(state)
        }

        override fun showUiState(state: FavoritesUiState) {
            states.add(state)
        }

        override fun showData(tracks: List<SelectedTrackUi>) {
            dataList.clear()
            dataList.addAll(tracks)
        }

        override suspend fun collectState(
            owner: LifecycleOwner,
            collector: FlowCollector<FavoritesUiState>,
        ) = Unit

        override suspend fun collectData(
            owner: LifecycleOwner,
            collector: FlowCollector<List<SelectedTrackUi>>,
        ) = Unit

        override suspend fun collectLoading(
            owner: LifecycleOwner,
            collector: FlowCollector<FavoritesUiState>,
        ) = Unit

    }

    class TestSelectedTrackInteractor: SelectedTracksInteractor{
        var isDbEmpty = false
        var addItem = false
        val list = emptyList<SelectedTrackUi>().toMutableList()

        override suspend fun isDbEmpty(): Boolean = isDbEmpty

        override suspend fun handleItem(item: SelectedTrackUi): List<SelectedTrackUi> {
            addItem = !addItem
            return list
        }

        override suspend fun map(
            sortingState: SortingState,
            playlistId: Int,
        ): Flow<List<SelectedTrackUi>> {
            return flow { emit(list) }
        }

        override suspend fun saveList(list: List<SelectedTrackUi>) {

        }

        override suspend fun selectedTracks(): List<SelectedTrackUi> {
            return list
        }

    }

    class TestSelectedTracksCommunication: SelectedTracksCommunication {
        val list = emptyList<SelectedTrackUi>().toMutableList()

        override suspend fun collect(
            lifecycleOwner: LifecycleOwner,
            collector: FlowCollector<List<SelectedTrackUi>>,
        ) = Unit

        override fun map(newValue: List<SelectedTrackUi>) {
            list.clear()
            list.addAll(newValue)
        }

        override suspend fun collectIgnoreLifecycle(collector: FlowCollector<List<SelectedTrackUi>>)  = Unit

    }
}