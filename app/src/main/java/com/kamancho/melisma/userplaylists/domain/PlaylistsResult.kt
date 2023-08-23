package com.kamancho.melisma.userplaylists.domain

/**
 * Created by HP on 13.07.2023.
 **/
sealed interface PlaylistsResult{

    interface Mapper<T>{

       suspend fun map(message: String,error: Boolean):T

    }

    suspend fun <T>map(mapper: Mapper<T>): T

    data class Success(
        private val message: String
    ): PlaylistsResult {

        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map(message,false)
    }

    data class Error(
        private val message: String
    ): PlaylistsResult {

        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map(message,true)
    }

    object Duplicated: PlaylistsResult {
        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map("",false)
    }

}