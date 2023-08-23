package com.example.musicapp.downloader.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 09.08.2023.
 **/
interface DownloadCompleteCommunication: Communication.Mutable<DownloadResult> {

    class Base @Inject constructor(): DownloadCompleteCommunication, Communication.UiUpdate<DownloadResult>(
        DownloadResult.Empty
    )
}