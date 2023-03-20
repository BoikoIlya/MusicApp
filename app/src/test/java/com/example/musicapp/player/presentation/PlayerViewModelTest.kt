package com.example.musicapp.player.presentation

import androidx.media3.common.Player.REPEAT_MODE_ONE
import com.example.musicapp.main.presentation.*
import com.example.musicapp.main.presentation.PlayerCommunicationState.ShuffleMode.Companion.ENABLE_SHUFFLE
import com.example.musicapp.trending.data.ObjectCreator
import com.example.musicapp.trending.presentation.TestTrendingViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

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


    @Before
    fun setup(){
        val dispatchersList = TestTrendingViewModel.TestDispatcherList()
        playbackPositionCommunication = TestTrackPlaybackPositionCommunication()
        playerControlsCommunication = TestPlayerControlsCommunication()
        currentQueueCommunication = TestCurrentQueueCommunication()
        selectedTrackCommunication = TestSelectedTrackCommunication()
        mediaController = TestMediaController()
        viewModel = PlayerViewModel(
            playerCommunication = PlayerCommunication.Base(
                playerControlsCommunication,
                currentQueueCommunication,
                selectedTrackCommunication,
                mediaController
            ),
            communication = playbackPositionCommunication
            , dispatchersList = dispatchersList,
            controller = mediaController,

        )
    }


    @Test
    fun `test player action`(){
        viewModel.playerAction(PlayerCommunicationState.Next)
        assertEquals(1, mediaController.nextTrackClicked)

        viewModel.playerAction(PlayerCommunicationState.Previous)
        assertEquals(1, mediaController.previousTrackClicked)


        viewModel.playerAction(PlayerCommunicationState.Pause)
        assertEquals(false, mediaController.isPlayingg)

        viewModel.playerAction(PlayerCommunicationState.Resume)
        assertEquals(true, mediaController.isPlayingg)

        viewModel.playerAction(PlayerCommunicationState.ShuffleMode(ENABLE_SHUFFLE))
        assertEquals(ENABLE_SHUFFLE, mediaController.shuffleModeEnabledd)

        viewModel.playerAction(PlayerCommunicationState.RepeatMode(REPEAT_MODE_ONE))
        assertEquals(ENABLE_SHUFFLE, mediaController.shuffleModeEnabledd)
    }

    @Test
    fun  `test duration seekbar`(){
        mediaController.durationn = 10
       val result = viewModel.durationSeekBar()
        assertEquals(10,result)
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
}