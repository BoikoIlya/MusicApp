package com.kamancho.melisma.favorites.data.cache

import com.kamancho.melisma.app.core.Mapper
import com.kamancho.melisma.trending.domain.TrackDomain
import javax.inject.Inject

/**
 * Created by HP on 09.07.2023.
 **/
interface DomainToDataIdsMapper: Mapper<TrackDomain,Pair<Int,Int>> {

    class Base @Inject constructor(
        private val mapper: TrackDomain.Mapper<Pair<Int,Int>>
    ): DomainToDataIdsMapper{

        override fun map(data: TrackDomain): Pair<Int,Int> {
            return data.map(mapper)
        }
    }
}