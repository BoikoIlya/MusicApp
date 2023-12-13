package com.kamancho.melisma.searchplaylistdetails.data

import com.kamancho.melisma.app.core.Mapper
import com.kamancho.melisma.favorites.data.cache.TrackCache
import com.kamancho.melisma.trending.domain.TrackDomain
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by HP on 15.08.2023.
 **/
interface TrackCacheToDomainMapper: Mapper<TrackCache, TrackDomain> {

    class Base @Inject constructor(): TrackCacheToDomainMapper {
        override fun map(data: TrackCache): TrackDomain {
            return TrackDomain(
                id = data.trackId.toInt(),
                track_url = data.url,
                smallImgUrl = data.smallImgUrl,
                bigImgUrl = data.bigImgUrl,
                name = data.name,
                artistName = data.artistName,
                albumName = data.albumName,
                date = data.date,
                durationInSeconds = TimeUnit.MILLISECONDS.toSeconds(data.durationInMillis.toLong()).toInt() ,
                ownerId = data.ownerId,
                isCached = false,
                artistsIds = data.artistsIds
            )
        }
    }
}