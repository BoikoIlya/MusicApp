package com.kamancho.melisma.frienddetails.data

import com.kamancho.melisma.app.core.Mapper
import com.kamancho.melisma.app.vkdto.SearchPlaylistItem
import com.kamancho.melisma.userplaylists.data.cache.PlaylistCache
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