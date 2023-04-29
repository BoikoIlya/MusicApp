package com.example.musicapp.player.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 21.03.2023.
 **/

interface IsSavedCommunication: Communication.MutableSingle<Boolean> {

    class Base @Inject constructor(): IsSavedCommunication, Communication.SingleUiUpdate<Boolean>()
}