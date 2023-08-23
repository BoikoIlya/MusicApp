package com.kamancho.melisma.notifications.domain

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