package com.kamancho.melisma.app.core

import com.kamancho.melisma.main.presentation.PlayerControlsState
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by HP on 31.01.2023.
 **/
interface PlayerControlsCommunication: Communication.Mutable<PlayerControlsState> {


    @Singleton
    class Base @Inject constructor(): Communication.UiUpdate<PlayerControlsState>(
        PlayerControlsState.Disabled),
        PlayerControlsCommunication
}
