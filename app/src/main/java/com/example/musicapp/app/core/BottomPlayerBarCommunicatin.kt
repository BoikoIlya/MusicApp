package com.example.musicapp.app.core

import com.example.musicapp.app.main.presentation.BottomPlayerBarState
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by HP on 31.01.2023.
 **/
interface BottomPlayerBarCommunicatin: Communication.Mutable<BottomPlayerBarState> {


    @Singleton
    class Base @Inject constructor(): Communication.UiUpdate<BottomPlayerBarState>(BottomPlayerBarState.Disabled),
        BottomPlayerBarCommunicatin
}
