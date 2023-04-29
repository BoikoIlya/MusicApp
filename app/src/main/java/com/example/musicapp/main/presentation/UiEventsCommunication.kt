package com.example.musicapp.main.presentation

import com.example.musicapp.app.core.Communication
import com.example.musicapp.app.core.UiEventState
import javax.inject.Inject

/**
 * Created by HP on 24.04.2023.
 **/
interface UiEventsCommunication: Communication.Mutable<UiEventState> {

    class Base @Inject constructor(): UiEventsCommunication, Communication.UiUpdate<UiEventState>(
        UiEventState.ClearCommunication
    )
}