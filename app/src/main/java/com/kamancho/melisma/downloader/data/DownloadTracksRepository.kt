package com.kamancho.melisma.downloader.data

import android.net.Uri
import com.kamancho.melisma.downloader.data.cache.DownloadTracksCacheDataSource
import com.kamancho.melisma.downloader.data.cloud.DownloadCloudDataSource
import javax.inject.Inject

/**
 * Created by HP on 22.08.2023.
 **/
interface DownloadTracksRepository {

    suspend fun download(cloudUri: Uri, name: String)

    suspend fun delete(name: String)

    class Base @Inject constructor(
        private val cache: DownloadTracksCacheDataSource,
        private val cloud: DownloadCloudDataSource
    ): DownloadTracksRepository {

        override suspend fun download(cloudUri: Uri,name: String) {
            val fileUri = cache.createFile(name)
            cloud.downloadTrack(name,fileUri,cloudUri)
        }

        override suspend fun delete(name: String) = cache.deleteTrack(name)
    }
}