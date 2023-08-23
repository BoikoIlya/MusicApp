package com.kamancho.melisma.settings.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 23.08.2023.
 **/
interface DownloadsPathCommunication: Communication.Mutable<String> {

    class Base @Inject constructor(): DownloadsPathCommunication, Communication.UiUpdate<String>("")
}