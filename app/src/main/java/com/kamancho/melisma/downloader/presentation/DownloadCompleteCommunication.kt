package com.kamancho.melisma.downloader.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 09.08.2023.
 **/
interface DownloadCompleteCommunication: Communication.Mutable<DownloadResult> {

    class Base @Inject constructor(): DownloadCompleteCommunication, Communication.UiUpdate<DownloadResult>(
        DownloadResult.Empty
    )
}