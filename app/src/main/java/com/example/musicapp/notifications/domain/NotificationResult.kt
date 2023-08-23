package com.example.musicapp.notifications.domain

import com.example.musicapp.trending.domain.TopBarItemDomain
import com.example.musicapp.trending.domain.TrackDomain
import com.example.musicapp.trending.presentation.TrendingResult

/**
 * Created by HP on 21.08.2023.
 **/
sealed interface NotificationResult {

    suspend fun <T>map(mapper: Mapper<T>):T

    interface Mapper<T>{
        suspend fun map(data: List<NotificationDomain>, message: String,preResult: Boolean): T
    }

    data class Success(
        private val data: List<NotificationDomain>,
        private val preResult: Boolean
    ): NotificationResult {
        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map(data, "",preResult)
    }

    data class Error(
        private val message: String,
    ): NotificationResult {
        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map(emptyList(), message,false)
    }


}