package com.example.musicapp.main.presentation

import android.view.View
import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 17.06.2023.
 **/
interface BottomNavCommunication: Communication.Mutable<Int> {

    class Base @Inject constructor(): BottomNavCommunication, Communication.UiUpdate<Int>(View.VISIBLE)
}