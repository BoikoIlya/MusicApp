package com.example.musicapp.favorites.data.cache

import com.example.musicapp.app.core.Mapper
import com.example.musicapp.trending.domain.TrackDomain
import javax.inject.Inject

/**
 * Created by HP on 09.07.2023.
 **/
interface DomainToContainsMapper: Mapper<TrackDomain,Pair<String,String>> {

    class Base @Inject constructor(
        private val mapper: TrackDomain.Mapper<Pair<String,String>>
    ): DomainToContainsMapper{

        override fun map(data: TrackDomain): Pair<String, String> {
            return data.map(mapper)
        }
    }
}