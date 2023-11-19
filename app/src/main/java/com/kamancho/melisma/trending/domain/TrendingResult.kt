package com.kamancho.melisma.trending.presentation

import com.kamancho.melisma.trending.domain.TopBarItemDomain
import com.kamancho.melisma.trending.domain.TrackDomain

/**
 * Created by HP on 27.01.2023.
 **/
sealed interface TrendingResult {

   suspend fun <T>map(mapper: Mapper<T>):T

    interface Mapper<T>{
        suspend fun map(data: Triple<List<TopBarItemDomain>, List<TrackDomain>,List<TopBarItemDomain>>, message: String): T
    }

    data class Success(private val data: Triple<List<TopBarItemDomain>, List<TrackDomain>,List<TopBarItemDomain>>): TrendingResult {
        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map(data, "")
    }

    data class Error(
        private val message: String,
        private val topBarItems: List<TopBarItemDomain>,
        private val embededVkPlaylists: List<TopBarItemDomain>,
    ): TrendingResult {
        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map(Triple(topBarItems, emptyList(),embededVkPlaylists), message)
    }

}