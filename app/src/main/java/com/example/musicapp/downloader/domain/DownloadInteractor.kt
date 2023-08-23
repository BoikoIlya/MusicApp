package com.example.musicapp.downloader.domain

import android.net.Uri
import android.util.Log
import com.example.musicapp.R
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.downloader.data.DownloadTracksRepository
import javax.inject.Inject

/**
 * Created by HP on 23.08.2023.
 **/
interface DownloadInteractor {

    suspend fun download(
        uri: Uri,
        title: String,
        author: String
        ):String

    suspend fun deleteTrack(title: String, author: String):String

    class Base @Inject constructor(
        private val repository: DownloadTracksRepository,
        private val managerResource: ManagerResource
    ): DownloadInteractor {

        override suspend fun download(uri: Uri, title: String, author: String): String {
           return try {
                    repository.download(uri,String.format("$title - $author"))
                    ""
                }catch (e: Exception){
                    if(e is SecurityException) managerResource.getString(R.string.give_storage_permission)
                    else managerResource.getString(R.string.fail_to_download)
                }
        }

        override suspend fun deleteTrack(title: String, author: String):String {
           return try {
                repository.delete(String.format("$title - $author"))
               ""
            }catch (e: Exception){

                if(e is NoSuchElementException) managerResource.getString(R.string.track_not_found_in_downlodas)
                else managerResource.getString(R.string.fail_to_delete)
            }
        }
    }
}