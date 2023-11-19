package com.kamancho.melisma.search.domain

import com.kamancho.melisma.trending.domain.TrackDomain

/**
 * Created by Ilya Boiko @camancho
on 25.10.2023.
 **/
interface SearchDomainState<T> {

    suspend fun map(mapper: Mapper<T>)

    interface Mapper<T> {
        suspend fun map(state: Success<T>, list: List<T>)
        suspend fun map(state: Error<T>, message: String)

    }


    data class Success<T>(
     private val list: List<T>,
    ) : SearchDomainState<T> {

        override suspend fun map(mapper: Mapper<T>) = mapper.map(this, list)
    }

    data class Error<T>(
     private val message: String,
    ) : SearchDomainState<T> {
        override suspend fun map(mapper: Mapper<T>) = mapper.map(this, message)

    }

}