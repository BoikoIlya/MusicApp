package com.kamancho.melisma.main.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 23.04.2023.
 **/
interface SlideViewPagerCommunication: Communication.MutableSingle<Int> {

    class Base @Inject constructor(): SlideViewPagerCommunication, Communication.SingleUiUpdate<Int>()
}