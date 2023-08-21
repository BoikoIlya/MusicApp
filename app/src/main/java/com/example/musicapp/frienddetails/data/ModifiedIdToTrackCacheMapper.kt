package com.example.musicapp.frienddetails.data

import com.example.musicapp.app.core.Mapper
import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.favorites.data.cache.TrackCache
import javax.inject.Inject

/**
 * Created by HP on 18.08.2023.
 **/
interface ModifiedIdToTrackCacheMapper: Mapper<Pair<String,List<TrackItem>>,List<TrackCache>> {

    class Base @Inject constructor(): ModifiedIdToTrackCacheMapper {

        override fun map(data: Pair<String, List<TrackItem>>): List<TrackCache> {
            val mapper = TrackItem.ModifiedIdToTrackCacheMapper(data.first)
            return data.second.map { it.map(mapper) }
        }
    }
}