package com.example.musicapp.favorites.presentation


import androidx.media3.common.MediaItem


sealed interface TracksResult {

    interface Mapper<T> {
        suspend fun map(message: String, list: List<MediaItem>): T

        suspend fun map(
            message: String,
            list: List<MediaItem>,
            albumDescription: String,
            albumName: String,
            albumImgUrl: String
            ):T
    }

    suspend fun <T> map(mapper: Mapper<T>):T

    data class Success(
        private val list: List<MediaItem> = emptyList(),
        private val message: String = ""
    ) : TracksResult {
        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map(message, list)
    }

    data class SuccessAlbumTracks(
        private val list: List<MediaItem>,
        private val albumDescription: String,
        private val albumName: String,
        private val albumImgUrl: String
    ): TracksResult {
        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map("",list, albumDescription, albumName, albumImgUrl)
    }

    data class Failure(private val message: String) : TracksResult {

        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map(message, emptyList())
    }

    object Duplicate: TracksResult {

        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map("", emptyList())

    }

}



