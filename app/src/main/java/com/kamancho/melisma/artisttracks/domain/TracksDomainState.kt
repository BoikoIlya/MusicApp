package com.kamancho.melisma.artisttracks.domain

import com.kamancho.melisma.trending.domain.TrackDomain

/**
 * Created by Ilya Boiko @camancho
on 09.12.2023.
 **/
sealed interface TracksDomainState {

    suspend fun <T> map(mapper: Mapper<T>): T

    interface Mapper<T> {
       suspend fun map(state: Success, list: List<TrackDomain>): T
       suspend fun map(state: Failure, message: String,reloadBtnVisibility: Boolean): T
    }

    data class Success(
     private val list: List<TrackDomain>,
    ) : TracksDomainState {
        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map(this, list)

    }

    data class Failure(
        private val message: String,
        private val reloadBtnVisibility: Boolean = true
    ) : TracksDomainState {
        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map(this, message, reloadBtnVisibility)
    }

}