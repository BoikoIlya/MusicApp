package com.kamancho.melisma.favoritesplaylistdetails.data.cache

import com.kamancho.melisma.app.core.Mapper
import com.kamancho.melisma.favorites.data.cache.TrackCache
import com.kamancho.melisma.userplaylists.data.cache.PlaylistCache
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