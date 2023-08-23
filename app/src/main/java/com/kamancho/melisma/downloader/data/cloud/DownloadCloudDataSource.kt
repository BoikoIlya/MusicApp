package com.kamancho.melisma.downloader.data.cloud

import android.app.DownloadManager
import android.net.Uri
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.ManagerResource
import javax.inject.Inject

/**
 * Created by HP on 23.08.2023.
 **/
interface DownloadCloudDataSource {

    suspend fun downloadTrack(name: String,fileUri: Uri, cloudUri: Uri)

    class Base @Inject constructor(
        private val managerResource: ManagerResource,
        private val downloadManager: DownloadManager
    ): DownloadCloudDataSource{

        override suspend fun downloadTrack(name: String,fileUri: Uri, cloudUri: Uri) {
            val request = DownloadManager.Request(cloudUri)
                .setTitle(name)
                .setDescription(managerResource.getString(R.string.downloaing))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setMimeType("audio/mpeg")
                .setDestinationUri(fileUri)

             downloadManager.enqueue(request)
        }

    }
}