package com.kamancho.melisma.downloader.domain

import android.net.Uri
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.ConnectionChecker
import com.kamancho.melisma.app.core.ManagerResource
import com.kamancho.melisma.downloader.data.DownloadTracksRepository
import javax.inject.Inject

/**
 * Created by HP on 23.08.2023.
 **/
interface DownloadInteractor {

    suspend fun download(
        uri: Uri?,
        title: String,
        author: String,
    ): String

    suspend fun deleteTrack(title: String, author: String): String

    class Base @Inject constructor(
        private val repository: DownloadTracksRepository,
        private val managerResource: ManagerResource,
        private val connectionChecker: ConnectionChecker,
    ) : DownloadInteractor {

        override suspend fun download(uri: Uri?, title: String, author: String): String {
            return try {
                if (!connectionChecker.isDeviceHaveConnection())
                    return managerResource.getString(R.string.no_connection_message)
                if (uri.toString().isEmpty() || uri == null)
                   return managerResource.getString(R.string.unavailable_track)
                repository.download(uri, String.format("$title - $author"))
                ""
            } catch (e: Exception) {
                if (e is SecurityException) managerResource.getString(R.string.give_storage_permission)
                else managerResource.getString(R.string.fail_to_download)
            }
        }

        override suspend fun deleteTrack(title: String, author: String): String {
            return try {
                repository.delete(String.format("$title - $author"))
                ""
            } catch (e: Exception) {

                if (e is NoSuchElementException) managerResource.getString(R.string.track_not_found_in_downlodas)
                else managerResource.getString(R.string.fail_to_delete)
            }
        }
    }
}