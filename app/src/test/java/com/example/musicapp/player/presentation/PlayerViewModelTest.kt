package com.example.musicapp.player.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.Player.REPEAT_MODE_ONE
import com.example.musicapp.core.testcore.TestTemporaryTracksCache
import com.example.musicapp.core.testcore.TestDispatcherList
import com.example.musicapp.core.testcore.TestSingleUiStateCommunication
import com.example.musicapp.favorites.presentation.FavoritesViewModelTest
import com.example.musicapp.favorites.presentation.TracksResultToSingleUiEventCommunicationMapper
import com.example.musicapp.main.presentation.*
import com.example.musicapp.trending.data.ObjectCreator
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.FlowCollector
import org.junit.Before
import org.junit.Test
import java.time.Duration

/**
 * Created by HP on 18.03.2023.
 **/
class PlayerViewModelTest: ObjectCreator() {

    lateinit var viewModel: PlayerViewModel
    lateinit var playerCommunication: PlayerCommunication

    lateinit var playerControlsCommunication: TestPlayerControlsCommunication
    lateinit var currentQueueCommunication: TestCurrentQueueCommunication
    lateinit var selectedTrackCommunication: TestSelectedTrackCommunication
    lateinit var mediaController: TestMediaController
    lateinit var playbackPositionCommunication: TestTrackPlaybackPositionCommunication
    lateinit var singleUiStateCommunication: TestSingleUiStateCommunication
    lateinit var isSavedCommunication: TestIsSavedCommunication
    lateinit var repository:  FavoritesViewModelTest.TestFavoriteRepository

    @Before
    fun setup(){
        val dispatchersList = TestDispatcherList()
        repository =  FavoritesViewModelTest.TestFavoriteRepository()
        singleUiStateCommunication = TestSingleUiStateCommunication()
        playbackPositionCommunication = TestTrackPlaybackPositionCommunication()
        playerControlsCommunication = TestPlayerControlsCommunication()
        currentQueueCommunication = TestCurrentQueueCommunication()
        selectedTrackCommunication = TestSelectedTrackCommunication()
        isSavedCommunication = TestIsSavedCommunication()
        mediaController = TestMediaController()
        viewModel = PlayerViewModel(
            playerCommunication = PlayerCommunication.Base(
                playerControlsCommunication =playerControlsCommunication,
                currentQueueCommunication = currentQueueCommunication,
                selectedTrackCommunication =selectedTrackCommunication,
                singleUiEventCommunication = singleUiStateCommunication,
                trackDurationCommunication = TrackDurationCommunication.Base(),
                controller = mediaController,
            ),
            communication = playbackPositionCommunication, dispatchersList = dispatchersList,
            controller = mediaController,
            isSavedCommunication = isSavedCommunication,
            favoriteTracksRepository = repository,
            mapper = TracksResultToSingleUiEventCommunicationMapper.Base(singleUiStateCommunication, UiEventsCommunication.Base()),
            bottomSheetCommunication = MainViewModelTest.TestBottomSheetCommunication(),
            slideViewPagerCommunication = SlideViewPagerCommunication.Base(),
            trackDurationCommunication = TrackDurationCommunication.Base(),
            temporaryTracksCache = TestTemporaryTracksCache(),

            )
    }




    @Test
    fun  `test duration for text view`(){
       val result = viewModel.durationForTextView(Duration.ofSeconds(61).toMillis())
        assertEquals("01:01",result)
    }

    @Test
    fun  `test is shuffle mode enabled`(){
        mediaController.shuffleModeEnabledd = true
        val result = viewModel.isShuffleModeEnabled()
        assertEquals(true,result)
    }

    @Test
    fun  `test is repeat enabled`(){
        mediaController.repeatModee = REPEAT_MODE_ONE
        val result = viewModel.isRepeatEnabled()
        assertEquals(true,result)
    }

    @Test
    fun `is saved`(){
        repository.list.addAll(listOf(getMediaItem("2")))
        viewModel.isSaved("2")
        assertEquals(true, isSavedCommunication.value)

        repository.list.clear()
        viewModel.isSaved("2")
        assertEquals(false, isSavedCommunication.value)
    }

    class TestIsSavedCommunication: IsSavedCommunication{

        var value = false
        override suspend fun collect(
            lifecycleOwner: LifecycleOwner,
            collector: FlowCollector<Boolean>,
        ) = Unit

        override suspend fun map(newValue: Boolean) {
            value = newValue
        }
    }

}