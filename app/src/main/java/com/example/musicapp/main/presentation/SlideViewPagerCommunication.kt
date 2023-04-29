package com.example.musicapp.main.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 23.04.2023.
 **/
interface SlideViewPagerCommunication: Communication.MutableSingle<Int> {

    class Base @Inject constructor(): SlideViewPagerCommunication, Communication.SingleUiUpdate<Int>()
}