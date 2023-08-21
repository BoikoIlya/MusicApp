package com.example.musicapp.frienddetails.data

import com.example.musicapp.app.core.Mapper
import com.example.musicapp.app.vkdto.SearchPlaylistItem
import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.userplaylists.data.cache.PlaylistCache
import javax.inject.Inject

/**
 * Created by HP on 19.08.2023.
 **/
interface ModifiedIdToPlaylistCacheMapper: Mapper<Pair<String, List<SearchPlaylistItem>>, List<PlaylistCache>> {

    class Base @Inject constructor(): ModifiedIdToPlaylistCacheMapper {

        override fun map(data: Pair<String, List<SearchPlaylistItem>>): List<PlaylistCache> {
            val mapper = SearchPlaylistItem.ToPlaylistCache(data.first)
            return data.second.map { it.map(mapper) }
        }
    }
}