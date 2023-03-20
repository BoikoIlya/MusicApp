package com.example.musicapp.main.presentation

import androidx.media3.common.MediaItem
import com.example.musicapp.trending.data.ObjectCreator
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 17.03.2023.
 **/
class MainViewModelTest: ObjectCreator() {


    lateinit var viewModel: MainViewModel
    lateinit var playerCommunication: PlayerCommunication

    lateinit var playerControlsCommunication: TestPlayerControlsCommunication
    lateinit var currentQueueCommunication: TestCurrentQueueCommunication
    lateinit var selectedTrackCommunication: TestSelectedTrackCommunication
    lateinit var mediaController: TestMediaController


    @Before
    fun setup(){
        playerControlsCommunication = TestPlayerControlsCommunication()
        currentQueueCommunication = TestCurrentQueueCommunication()
        selectedTrackCommunication = TestSelectedTrackCommunication()
        mediaController = TestMediaController()
        viewModel = MainViewModel(
            playerCommunication = PlayerCommunication.Base(
                playerControlsCommunication,
                currentQueueCommunication,
                selectedTrackCommunication,
                mediaController
            )
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
    }
}