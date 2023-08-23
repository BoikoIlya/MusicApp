package com.kamancho.melisma.player.presentation

import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_ONE
import com.kamancho.melisma.app.core.ConnectionChecker
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.app.core.TrackChecker
import com.kamancho.melisma.app.core.TracksResultToUiEventCommunicationMapper
import com.kamancho.melisma.favorites.testcore.TestDispatcherList
import com.kamancho.melisma.favorites.testcore.TestFavoriteRepository
import com.kamancho.melisma.favorites.testcore.TestSingleUiStateCommunication
import com.kamancho.melisma.favorites.presentation.FavoritesViewModelTest
import com.kamancho.melisma.favorites.testcore.TestFavoritesTracksInteractor
import com.kamancho.melisma.favorites.testcore.TestManagerResource
import com.kamancho.melisma.main.presentation.*
import com.kamancho.melisma.trending.data.ObjectCreator
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Duration

/**
 * Created by HP on 18.03.2023.
 **/
class PlayerViewModelTest: ObjectCreator() {

    private lateinit var viewModel: PlayerViewModel
    private lateinit var playerCommunication: PlayerCommunication

    private lateinit var playerControlsCommunication: TestPlayerControlsCommunication
    private lateinit var currentQueueCommunication: TestCurrentQueueCommunication
    private lateinit var selectedTrackCommunication: TestSelectedTrackCommunication
    private lateinit var mediaController: TestMediaController
    private lateinit var playbackPositionCommunication: TestTrackPlaybackPositionCommunication
    private lateinit var singleUiStateCommunication: TestSingleUiStateCommunication
    private lateinit var playingTrackIdCommunication: FavoritesViewModelTest.TestPlyingTrackIdCommuniacation
    private lateinit var repository: TestFavoriteRepository
    private lateinit var favoritesInteractor: TestFavoritesTracksInteractor
    private lateinit var managerResource: TestManagerResource
    private lateinit var connectionChecker: ConnectionChecker

    @Before
    fun setup(){
        playingTrackIdCommunication = FavoritesViewModelTest.TestPlyingTrackIdCommuniacation()
        managerResource = TestManagerResource()
        val dispatchersList = TestDispatcherList()
        favoritesInteractor = TestFavoritesTracksInteractor()
        repository =  TestFavoriteRepository()
        singleUiStateCommunication = TestSingleUiStateCommunication()
        playbackPositionCommunication = TestTrackPlaybackPositionCommunication()
        playerControlsCommunication = TestPlayerControlsCommunication()
        currentQueueCommunication = TestCurrentQueueCommunication()
        selectedTrackCommunication = TestSelectedTrackCommunication()
        connectionChecker = FavoritesViewModelTest.TestConnectionChecker()
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
            mapper = TracksResultToUiEventCommunicationMapper.Base(singleUiStateCommunication,playingTrackIdCommunication),
            bottomSheetCommunication = MainViewModelTest.TestBottomSheetCommunication(),
            slideViewPagerCommunication = SlideViewPagerCommunication.Base(),
            favoritesInteractor = favoritesInteractor,
            singleUiEventCommunication = singleUiStateCommunication,
            managerResource = managerResource,
            playingTrackIdCommunication = playingTrackIdCommunication,
            trackChecker = TrackChecker.Base(singleUiStateCommunication,managerResource,connectionChecker),

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
    fun `test launch delete item dialog`(){
        favoritesInteractor.containsTrackInDb = false
        viewModel.launchDeleteItemDialog(1, MediaItem.EMPTY)
        assertEquals(SingleUiEventState.ShowSnackBar.Error::class,singleUiStateCommunication.stateList.last()::class)

        favoritesInteractor.containsTrackInDb = true
        viewModel.launchDeleteItemDialog(1, MediaItem.EMPTY)
        assertEquals(SingleUiEventState.ShowDialog::class,singleUiStateCommunication.stateList.last()::class)
    }


}