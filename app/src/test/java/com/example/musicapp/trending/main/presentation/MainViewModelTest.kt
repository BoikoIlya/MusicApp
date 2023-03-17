package com.example.musicapp.trending.main.presentation

import com.example.musicapp.app.main.presentation.MainViewModel
import com.example.musicapp.app.main.presentation.PlayerCommunicationState
import com.example.musicapp.trending.trending.data.ObjectCreator
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 17.03.2023.
 **/
class MainViewModelTest: ObjectCreator() {


    lateinit var viewModel: MainViewModel
    lateinit var communication: TestPlayerCommunication


    @Before
    fun setup(){
        communication = TestPlayerCommunication()
        viewModel = MainViewModel(
            playerCommunication = communication
        )
    }

    @Test
    fun `test player action`(){
        viewModel.playerAction(PlayerCommunicationState.Previous)
        assertEquals(PlayerCommunicationState.Previous, communication.stateList[0])

        viewModel.playerAction(PlayerCommunicationState.Play(getMediaItem(),0))
        assertEquals(PlayerCommunicationState.Play(getMediaItem(),0), communication.stateList[1])

        viewModel.playerAction(PlayerCommunicationState.Pause(getMediaItem()))
        assertEquals(PlayerCommunicationState.Pause(getMediaItem()), communication.stateList[2])
    }
}