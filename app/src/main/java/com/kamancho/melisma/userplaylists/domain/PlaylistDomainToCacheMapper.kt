package com.kamancho.melisma.userplaylists.domain

import com.kamancho.melisma.app.core.Mapper
import com.kamancho.melisma.userplaylists.data.cache.PlaylistCache
import javax.inject.Inject

/**
 * Created by HP on 12.07.2023.
 **/
interface PlaylistDomainToCacheMapper: Mapper<PlaylistDomain,PlaylistCache> {

    class Base @Inject constructor(
        private val mapper: PlaylistDomain.Mapper<PlaylistCache>
    ): PlaylistDomainToCacheMapper{

        override fun map(data: PlaylistDomain): PlaylistCache = data.map(mapper)
    }
}