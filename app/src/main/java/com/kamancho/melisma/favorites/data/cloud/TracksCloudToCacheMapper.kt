package com.kamancho.melisma.favorites.data.cloud

import com.kamancho.melisma.app.core.Mapper
import com.kamancho.melisma.app.vkdto.TrackItem
import com.kamancho.melisma.favorites.data.cache.TrackCache
import javax.inject.Inject

/**
 * Created by HP on 09.07.2023.
 **/
interface TracksCloudToCacheMapper: Mapper<List<TrackItem>, List<TrackCache>> {

    class Base @Inject constructor(
        private val mapper: TrackItem.Mapper<TrackCache>
    ): TracksCloudToCacheMapper {

        override fun map(data: List<TrackItem>): List<TrackCache> {
            return data.map { it.map(mapper) }
        }
    }
}