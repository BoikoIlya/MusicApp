package com.example.musicapp.app.core

import com.example.musicapp.addtoplaylist.domain.SelectedTrackDomain
import com.example.musicapp.favorites.data.cache.TrackCache
import javax.inject.Inject

/**
 * Created by HP on 26.07.2023.
 **/
interface TracksCacheToSelectedTracksDomainMapper: Mapper<List<TrackCache>,List<SelectedTrackDomain>> {

    class Base @Inject constructor(): TracksCacheToSelectedTracksDomainMapper {
        override fun map(data: List<TrackCache>): List<SelectedTrackDomain> {
            return data.map {
                SelectedTrackDomain(
                    id = it.trackId,
                    title = it.name,
                    author = it.artistName,
                    durationFormatted = it.durationFormatted,
                    smallImageUrl = it.smallImgUrl
                 )
            }
        }
    }
}