package com.example.musicapp.favorites.presentation


import androidx.media3.common.MediaItem


sealed interface TracksResult {

    interface Mapper<T> {
        suspend fun map(message: String, list: List<MediaItem>,error: Boolean,newId:Int): T

    }

    suspend fun <T> map(mapper: Mapper<T>):T

    data class Success(
        private val list: List<MediaItem> = emptyList(),
        private val message: String = "",
        private val newId: Int =-1
    ) : TracksResult {
        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map(message, list,false,newId)
    }



    data class Failure(private val message: String) : TracksResult {

        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map(message, emptyList(),true,-1)
    }


    object Duplicate: TracksResult {

        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map("", emptyList(),false,-1)

    }

}



