package com.example.musicapp.trending.presentation

import com.example.musicapp.trending.domain.TopBarItemDomain
import com.example.musicapp.trending.domain.TrackDomain

/**
 * Created by HP on 27.01.2023.
 **/
sealed interface TrendingResult {

   suspend fun <T>map(mapper: Mapper<T>):T

    interface Mapper<T>{
        suspend fun map(data: Pair<List<TopBarItemDomain>, List<TrackDomain>>, message: String): T
    }

    data class Success(private val data: Pair<List<TopBarItemDomain>, List<TrackDomain>>): TrendingResult {
        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map(data, "")
    }

    data class Error(
        private val message: String,
        private val topBarItems: List<TopBarItemDomain>
    ): TrendingResult {
        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map(Pair(topBarItems, emptyList()), message)
    }

}