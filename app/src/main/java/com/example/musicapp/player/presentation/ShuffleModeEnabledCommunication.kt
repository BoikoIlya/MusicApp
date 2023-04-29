package com.example.musicapp.player.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 09.04.2023.
 **/
interface ShuffleModeEnabledCommunication: Communication.Mutable<Boolean> {

    class Base @Inject constructor(): ShuffleModeEnabledCommunication, Communication.UiUpdate<Boolean>(false)
}