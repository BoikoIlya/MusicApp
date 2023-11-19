package com.kamancho.melisma.downloader.data.cache

import android.net.Uri
import android.os.Environment
import android.util.Log
import kotlinx.coroutines.flow.first
import java.io.File
import javax.inject.Inject

/**
 * Created by HP on 22.08.2023.
 **/
interface DownloadTracksCacheDataSource {

    suspend fun createFile(name: String): Uri

    suspend fun readListOfFileNamesAndPaths(): Map<String, String>

    suspend fun deleteTrack(name: String)

    suspend fun clearAllTracks()

    suspend fun downloadsSize(): Long


    class Base @Inject constructor(
        private val folderPathStore: DownloadFolderPathStore,
    ) : DownloadTracksCacheDataSource {

        companion object {
            const val defaultDownloadsFolderName = "MELISMA"
            const val fileExtension = ".mp3"
        }

        private val pattern = "[/:*?\"<>|]".toRegex()

        override suspend fun createFile(name: String): Uri {
            val savedPath = folderPathStore.read().first()
            val folder =
                if (savedPath.isEmpty())
                    File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        defaultDownloadsFolderName
                    )
                else File(savedPath)

            if (!folder.exists()) folder.mkdirs()


            val nameWithRemovedWrongCharacters = name
                .replace(pattern, "_")

            val mediaFile = File(folder, nameWithRemovedWrongCharacters + fileExtension)
            return Uri.fromFile(mediaFile)
        }

        override suspend fun readListOfFileNamesAndPaths(): Map<String, String> {
            val savedPath = folderPathStore.read().first()
            val folder =
                if (savedPath.isEmpty())
                    File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        defaultDownloadsFolderName
                    )
                else File(savedPath)
            val map = emptyMap<String,String>().toMutableMap()

           val files = folder.listFiles()

            files?.forEach { map[it.name] = it.path }

            return map
        }

        override suspend fun deleteTrack(name: String) {
            val savedPath = folderPathStore.read().first()
            val folder =
                if (savedPath.isEmpty())
                    File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        defaultDownloadsFolderName
                    )
                else File(savedPath)

            val nameWithRemovedWrongCharacters = name
                .replace(pattern, "_")
            val itemToDelete = folder.listFiles()?.find { it.name.contains(nameWithRemovedWrongCharacters) }
            itemToDelete?.delete() ?: throw NoSuchElementException()
        }

        override suspend fun clearAllTracks() {
            val savedPath = folderPathStore.read().first()
            val folder =
                if (savedPath.isEmpty())
                    File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        defaultDownloadsFolderName
                    )
                else File(savedPath)

            folder.deleteRecursively()
        }

        override suspend fun downloadsSize(): Long {
            val savedPath = folderPathStore.read().first()
            val folder =
                if (savedPath.isEmpty())
                    File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        defaultDownloadsFolderName
                    )
                else File(savedPath)

            var totalSizeInBytes: Long = 0
            folder.listFiles()?.forEach {
                totalSizeInBytes += it.length()
            }
            return totalSizeInBytes
        }
    }


}