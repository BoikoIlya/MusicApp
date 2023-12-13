package com.kamancho.melisma.artisttracks.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by Ilya Boiko @camancho
on 10.12.2023.
 **/
interface PageChangerCommunication: Communication.Mutable<String> {

    class Base @Inject constructor(): PageChangerCommunication,Communication.UiUpdate<String>("")
}