package com.example.musicapp.trending.presentation

import com.example.musicapp.trending.domain.PlaylistDomain
import com.example.musicapp.trending.domain.TrackDomain

/**
 * Created by HP on 27.01.2023.
 **/
sealed interface TrendingResult {

    fun <T>map(mapper: Mapper<T>):T

    interface Mapper<T>{
        fun map(data: Pair<List<PlaylistDomain>, List<TrackDomain>>, message: String): T
    }

    data class Success(private val data: Pair<List<PlaylistDomain>, List<TrackDomain>>): TrendingResult {
        override fun <T> map(mapper: Mapper<T>): T = mapper.map(data, "")
    }

    data class Error(private val message: String): TrendingResult {
        override fun <T> map(mapper: Mapper<T>): T = mapper.map(Pair(emptyList(), emptyList()), message)
    }

}