package com.example.musicapp.main.domain

import androidx.media3.common.MediaItem

/**
 * Created by HP on 15.03.2023.
 **/

sealed interface QueryResult {

    interface Mapper<T>{
        fun map(list: List<MediaItem>, errorMessage: String):T
    }

    fun <T> map(mapper: Mapper<T>):T

    data class Updated(private val list: List<MediaItem> = emptyList()): QueryResult {
        override fun <T> map(mapper: Mapper<T>): T  = mapper.map(list, "")
    }

    object NoUpdate: QueryResult {
        override fun <T> map(mapper: Mapper<T>): T  = mapper.map(emptyList(), "")
    }

}