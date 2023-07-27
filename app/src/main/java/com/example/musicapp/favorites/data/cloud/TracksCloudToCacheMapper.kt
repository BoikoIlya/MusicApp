package com.example.musicapp.favorites.data.cloud

import com.example.musicapp.app.core.Mapper
import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.favorites.data.cache.TrackCache
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