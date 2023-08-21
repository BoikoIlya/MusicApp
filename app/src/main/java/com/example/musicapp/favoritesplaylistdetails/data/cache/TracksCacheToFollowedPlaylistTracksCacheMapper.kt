package com.example.musicapp.favoritesplaylistdetails.data.cache

import com.example.musicapp.app.core.Mapper
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.userplaylists.data.cache.PlaylistCache
import javax.inject.Inject

/**
 * Created by HP on 16.08.2023.
 **/
interface TracksCacheToFollowedPlaylistTracksCacheMapper: Mapper<Pair<PlaylistCache,List<TrackCache>>, List<TrackCache>> {


    class Base @Inject constructor(): TracksCacheToFollowedPlaylistTracksCacheMapper {

        override fun map(data: Pair<PlaylistCache, List<TrackCache>>): List<TrackCache> {

            return data.second.map { it.copy(
                trackId = if(data.first.is_following) it.trackId+data.first.playlistId else it.trackId
            ) }
        }

    }
}