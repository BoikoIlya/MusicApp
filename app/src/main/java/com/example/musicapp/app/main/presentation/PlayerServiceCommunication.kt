package com.example.musicapp.app.main.presentation

import com.example.musicapp.app.core.Communication
import com.example.musicapp.player.presentation.PlayerServiceState
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by HP on 31.01.2023.
 **/
interface PlayerServiceCommunication: Communication.Mutable<PlayerServiceState> {


    @Singleton
    class Base @Inject constructor(): Communication.UiUpdate<PlayerServiceState>(PlayerServiceState.Disabled),
        PlayerServiceCommunication
}