package com.example.musicapp.favorites.data.cache

import com.example.musicapp.app.core.Mapper
import com.example.musicapp.trending.domain.TrackDomain
import javax.inject.Inject

/**
 * Created by HP on 09.07.2023.
 **/
interface TrackDomainToCacheMapper: Mapper<TrackDomain,TrackCache> {

    class Base @Inject constructor(
        private val mapper: TrackDomain.Mapper<TrackCache>
    ): TrackDomainToCacheMapper{

        override fun map(data: TrackDomain): TrackCache {
            return data.map(mapper)
        }
    }
}