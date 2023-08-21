package com.example.musicapp.editplaylist.presentation

import androidx.lifecycle.LifecycleOwner
import com.example.musicapp.addToPlaylist.presentation.AddToPlaylistViewModelTest
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.createplaylist.presentation.CreatePlaylistViewModelTest
import com.example.musicapp.creteplaylist.presentation.PlaylistDataResultMapper
import com.example.musicapp.creteplaylist.presentation.PlaylistDataSaveBtnUiState
import com.example.musicapp.creteplaylist.presentation.PlaylistDataUiState
import com.example.musicapp.editplaylist.domain.PlaylistDetailsInteractor
import com.example.musicapp.favorites.testcore.TestManagerResource
import com.example.musicapp.favorites.testcore.TestSingleUiStateCommunication
import com.example.musicapp.trending.data.ObjectCreator
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import com.example.musicapp.userplaylists.domain.PlaylistsResult
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 02.08.2023.
 **/
class EditPlaylistViewModelTest: ObjectCreator() {

    private lateinit var viewModel: EditPlaylistViewModel
    private lateinit var singleUiEventCommunication: TestSingleUiStateCommunication
    private lateinit var selectedTrackCommunication: AddToPlaylistViewModelTest.TestSelectedTracksCommunication
    private lateinit var saveButtonUiCommunication: CreatePlaylistViewModelTest.TestPlaylistSaveButtonUiCommunication
    private lateinit var selectedTrackInteractor: AddToPlaylistViewModelTest.TestSelectedTrackInteractor
    private lateinit var playlistDataUiStateCommunication: CreatePlaylistViewModelTest.TestPlaylistDataUiStateCommunication
    private lateinit var playlistDataInteractor: CreatePlaylistViewModelTest.TestPlaylistDataInteractor
    private lateinit var transfer: DataTransfer<PlaylistDomain>
    private lateinit var managerResource: TestManagerResource
    private lateinit var playlistTracksInteractor: TestPlaylistTracksInteractor
    private lateinit var titleStateCommunication: TestTitleStateCommunication

    @Before
    fun setup(){
        playlistTracksInteractor = TestPlaylistTracksInteractor()
        titleStateCommunication = TestTitleStateCommunication()
        playlistDataInteractor = CreatePlaylistViewModelTest.TestPlaylistDataInteractor()
        managerResource = TestManagerResource()
        transfer = DataTransfer.PlaylistTransfer()
        transfer.save(getPlaylistDomain(title = "hi"),)
        playlistDataInteractor = CreatePlaylistViewModelTest.TestPlaylistDataInteractor()
        playlistDataUiStateCommunication =
            CreatePlaylistViewModelTest.TestPlaylistDataUiStateCommunication()
        selectedTrackInteractor = AddToPlaylistViewModelTest.TestSelectedTrackInteractor()
        selectedTrackInteractor.list.add(getSelectedTrackUi())
        selectedTrackCommunication = AddToPlaylistViewModelTest.TestSelectedTracksCommunication()
        saveButtonUiCommunication =
            CreatePlaylistViewModelTest.TestPlaylistSaveButtonUiCommunication()
        singleUiEventCommunication = TestSingleUiStateCommunication()
        viewModel = EditPlaylistViewModel(
            selectedTracksCommunication = selectedTrackCommunication,
            dispatchersList = DispatchersList.Base(),
            playlistSaveBtnUiStateCommunication =saveButtonUiCommunication,
            playlistUiStateCommunication = playlistDataUiStateCommunication,
            interactor = playlistDataInteractor,
            selectedTracksInteractor = selectedTrackInteractor,
            transfer = transfer,
            toPlaylistUiMapper = PlaylistDomain.ToPlaylistUi(),
            titleStateCommunication = titleStateCommunication,
            playlistTracksInteractor = playlistTracksInteractor,
            playlistResultEditPlaylistUpdateMapper = EditPlaylistUpdateMapper.Base(
                playlistDataUiStateCommunication,saveButtonUiCommunication
            ),
            playlistDataResultMapper =  PlaylistDataResultMapper.Base(
                globalSingleUiEventCommunication = singleUiEventCommunication,
                playlistDataUiStateCommunication = playlistDataUiStateCommunication,
                saveBtnUiStateCommunication =saveButtonUiCommunication,
            ),
            managerResource = managerResource,
            toIdMapper = PlaylistUi.ToIdMapper()
        )
    }


    @Test
    fun `test init`()= runBlocking{
        delay(100)
        assertEquals(PlaylistDataSaveBtnUiState.Hide::class,saveButtonUiCommunication.states[0]::class)
        assertEquals(PlaylistDataUiState.Loading::class, playlistDataUiStateCommunication.states[1]::class)
        assertEquals(0,playlistTracksInteractor.id)
        assertEquals(selectedTrackInteractor.list.size,selectedTrackCommunication.list.size)

    }

    @Test
    fun `test verify`()= runBlocking {


        viewModel.verify("","")
        delay(1)
        assertEquals(TitleUiState.Error::class,titleStateCommunication.states[0]::class)
        assertEquals(PlaylistDataSaveBtnUiState.Hide::class,saveButtonUiCommunication.states[1]::class)


        viewModel.verify("","ds")
        delay(1)
        assertEquals(TitleUiState.Error::class,titleStateCommunication.states[1]::class)
        assertEquals(PlaylistDataSaveBtnUiState.Hide::class,saveButtonUiCommunication.states[2]::class)

        selectedTrackInteractor.list.add(getSelectedTrackUi())
        viewModel.verify("hi","")
        delay(1)
        assertEquals(TitleUiState.Success::class,titleStateCommunication.states[2]::class)
        assertEquals(PlaylistDataSaveBtnUiState.Show::class,saveButtonUiCommunication.states[3]::class)
    }

    @Test
    fun `test save`() = runBlocking {
        viewModel.save("","")
        assertEquals(PlaylistDataUiState.Loading::class, playlistDataUiStateCommunication.states[1]::class)
    }

    class TestTitleStateCommunication: TitleStateCommunication {
        val states = mutableListOf<TitleUiState>().toMutableList()

        override suspend fun collect(
            lifecycleOwner: LifecycleOwner,
            collector: FlowCollector<TitleUiState>,
        ) = Unit

        override fun map(newValue: TitleUiState) {
            states.add(newValue)
        }

        override suspend fun collectIgnoreLifecycle(collector: FlowCollector<TitleUiState>) = Unit

    }

    class TestPlaylistTracksInteractor: PlaylistDetailsInteractor{
        var result = PlaylistsResult.Success("")
        var id = -1

        override suspend fun fetch(playlistId: Int): PlaylistsResult {
            id = playlistId
            return result
        }

    }
}