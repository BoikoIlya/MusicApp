package com.kamancho.melisma.createplaylist.presentation

import androidx.lifecycle.LifecycleOwner
import com.kamancho.melisma.addToPlaylist.presentation.AddToPlaylistViewModelTest
import com.kamancho.melisma.addtoplaylist.presentation.SelectedTrackUi
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.PlaylistDataInteractor
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.creteplaylist.presentation.CreatePlaylistViewModel
import com.kamancho.melisma.creteplaylist.presentation.PlaylistDataResultMapper
import com.kamancho.melisma.creteplaylist.presentation.PlaylistDataSaveBtnUiState
import com.kamancho.melisma.creteplaylist.presentation.PlaylistDataUiState
import com.kamancho.melisma.creteplaylist.presentation.PlaylistDataUiStateCommunication
import com.kamancho.melisma.creteplaylist.presentation.PlaylistSaveBtnUiStateCommunication
import com.kamancho.melisma.favorites.testcore.TestSingleUiStateCommunication
import com.kamancho.melisma.trending.data.ObjectCreator
import com.kamancho.melisma.userplaylists.domain.PlaylistsResult
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 02.08.2023.
 **/
class CreatePlaylistViewModelTest: ObjectCreator() {

    private lateinit var viewModel: CreatePlaylistViewModel
    private lateinit var singleUiEventCommunication: TestSingleUiStateCommunication
    private lateinit var selectedTrackCommunication: AddToPlaylistViewModelTest.TestSelectedTracksCommunication
    private lateinit var saveButtonUiCommunication: TestPlaylistSaveButtonUiCommunication
    private lateinit var selectedTrackInteractor: AddToPlaylistViewModelTest.TestSelectedTrackInteractor
    private lateinit var playlistDataUiStateCommunication: TestPlaylistDataUiStateCommunication
    private lateinit var playlistDataInteractor: TestPlaylistDataInteractor

    @Before
    fun setup(){
        playlistDataInteractor = TestPlaylistDataInteractor()
        playlistDataUiStateCommunication = TestPlaylistDataUiStateCommunication()
        selectedTrackInteractor = AddToPlaylistViewModelTest.TestSelectedTrackInteractor()
        selectedTrackCommunication = AddToPlaylistViewModelTest.TestSelectedTracksCommunication()
        saveButtonUiCommunication = TestPlaylistSaveButtonUiCommunication()
        singleUiEventCommunication = TestSingleUiStateCommunication()
        viewModel = CreatePlaylistViewModel(
            selectedTracksCommunication = selectedTrackCommunication,
            dispatchersList = DispatchersList.Base(),
            playlistSaveBtnUiStateCommunication =saveButtonUiCommunication,
            playlistUiStateCommunication = playlistDataUiStateCommunication,
            interactor = playlistDataInteractor,
            selectedTracksInteractor = selectedTrackInteractor,
            mapper = PlaylistDataResultMapper.Base(
                globalSingleUiEventCommunication = singleUiEventCommunication,
                playlistDataUiStateCommunication = playlistDataUiStateCommunication,
                saveBtnUiStateCommunication =saveButtonUiCommunication,
            )
        )
    }

    @Test
    fun `test verify`(){

        viewModel.verifyData("")
        assertEquals(PlaylistDataSaveBtnUiState.Hide::class,saveButtonUiCommunication.states[0]::class)

        viewModel.verifyData("a")
        assertEquals(PlaylistDataUiState.Success::class, playlistDataUiStateCommunication.states[0]::class)
        assertEquals(PlaylistDataSaveBtnUiState.Show::class,saveButtonUiCommunication.states[1]::class)


    }

    @Test
    fun `test init`(){

        assertEquals(PlaylistDataUiState.Success::class, playlistDataUiStateCommunication.states[0]::class)
    }

    @Test
    fun `test save`() = runBlocking{
        val initilaData = Pair("d "," d")
        val expectedData = Pair("d","d")

        viewModel.save(initilaData.first,initilaData.second)
        delay(1)
        assertEquals(PlaylistDataUiState.Loading::class, playlistDataUiStateCommunication.states[1]::class)
        assertEquals(PlaylistDataSaveBtnUiState.Hide::class,saveButtonUiCommunication.states[0]::class)
        assertEquals(expectedData.first,playlistDataInteractor.title)
        assertEquals(expectedData.second,playlistDataInteractor.description)
        assertEquals(PlaylistDataUiState.PopFragment::class,playlistDataUiStateCommunication.states[2]::class)
        assertEquals(SingleUiEventState.ShowSnackBar.Success::class,singleUiEventCommunication.stateList[0]::class)

        val message = "message"
        playlistDataInteractor.result = PlaylistsResult.Error(message)

        viewModel.save(initilaData.first,initilaData.second)

        delay(1)
        assertEquals(PlaylistDataUiState.Loading::class, playlistDataUiStateCommunication.states[3]::class)
        assertEquals(PlaylistDataSaveBtnUiState.Hide::class,saveButtonUiCommunication.states[1]::class)
        assertEquals(expectedData.first,playlistDataInteractor.title)
        assertEquals(expectedData.second,playlistDataInteractor.description)
        assertEquals(PlaylistDataUiState.DisableLoading::class,playlistDataUiStateCommunication.states[4]::class)
        assertEquals(SingleUiEventState.ShowSnackBar.Error::class,singleUiEventCommunication.stateList[1]::class)
        assertEquals(PlaylistDataSaveBtnUiState.Show::class,saveButtonUiCommunication.states[2]::class)
    }




    class TestPlaylistSaveButtonUiCommunication: PlaylistSaveBtnUiStateCommunication{
        val states = mutableListOf<PlaylistDataSaveBtnUiState>().toMutableList()

        override suspend fun collect(
            lifecycleOwner: LifecycleOwner,
            collector: FlowCollector<PlaylistDataSaveBtnUiState>,
        ) = Unit

        override fun map(newValue: PlaylistDataSaveBtnUiState) {
            states.add(newValue)
        }

        override suspend fun collectIgnoreLifecycle(collector: FlowCollector<PlaylistDataSaveBtnUiState>) = Unit

    }

    class TestPlaylistDataUiStateCommunication: PlaylistDataUiStateCommunication{
        val states = mutableListOf<PlaylistDataUiState>().toMutableList()

        override suspend fun collect(
            lifecycleOwner: LifecycleOwner,
            collector: FlowCollector<PlaylistDataUiState>,
        ) = Unit

        override fun map(newValue: PlaylistDataUiState) {
            states.add(newValue)
        }

        override suspend fun collectIgnoreLifecycle(collector: FlowCollector<PlaylistDataUiState>)  = Unit

    }

    class TestPlaylistDataInteractor: PlaylistDataInteractor{
        var title = ""
        var description =""
        var playlistId = 0
        val list = emptyList<SelectedTrackUi>().toMutableList()
        var result: PlaylistsResult = PlaylistsResult.Success("")

        override suspend fun editPlaylist(
            playlistId: Int,
            title: String,
            description: String,
            initialTracks: List<SelectedTrackUi>,
        ): PlaylistsResult {
           this.playlistId = playlistId
            this.title = title
            this.description = description
            this.list.addAll(initialTracks)
            return result
        }

        override suspend fun createPlaylist(title: String, description: String): PlaylistsResult {
            this.description = description
            this.title = title
            return result
        }

        override suspend fun followPlaylist(
            playlistId: Int,
            playlistOwnerId: Int,
        ): PlaylistsResult {
            this.playlistId = playlistId
            return result
        }

    }
}