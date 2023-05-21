package com.example.musicapp.searchhistory.data

import com.example.musicapp.searchhistory.data.cache.HistoryItemCache

/**
 * Created by HP on 06.05.2023.
 **/
sealed interface HistoryDeleteResult{

   suspend fun <T>map(mapper: Mapper<T>):T

    interface Mapper<T>{
       suspend fun map(message: String, data: List<HistoryItemCache>): T
    }

    data class Success(
        private val data: List<HistoryItemCache>,
        private val message: String
    ): HistoryDeleteResult{

        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map(message, data)
    }

    data class Error(
        private val message: String
    ): HistoryDeleteResult{

        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map(message, emptyList())
    }
}

