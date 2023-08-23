package com.kamancho.melisma.search.data.cloud

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.Mapper
import com.kamancho.melisma.app.vkdto.TrackItem
import com.kamancho.melisma.app.vkdto.TracksCloud
import javax.inject.Inject

/**
 * Created by HP on 14.08.2023.
 **/
interface TracksCloudToMediaItemsMapper: Mapper<TracksCloud,List<MediaItem>> {

    class Base @Inject constructor(
        private val mapper: TrackItem.Mapper<MediaItem>
    ): TracksCloudToMediaItemsMapper {

        override fun map(data: TracksCloud): List<MediaItem> {
            return data.response.items.map { it.map(mapper) }
        }
    }
}