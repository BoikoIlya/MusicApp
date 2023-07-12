package com.example.musicapp.favorites.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 26.06.2023.
 **/
interface ResetSwipeActionCommunication: Communication.MutableSingle<Unit> {

    class Base @Inject constructor(): ResetSwipeActionCommunication, Communication.SingleUiUpdate<Unit>()
}