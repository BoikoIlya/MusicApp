package com.kamancho.melisma.userplaylists.data.cloud

import com.kamancho.melisma.app.core.Mapper
import com.kamancho.melisma.app.vkdto.PlaylistItem
import com.kamancho.melisma.userplaylists.data.cache.PlaylistCache
import javax.inject.Inject

/**
 * Created by HP on 13.07.2023.
 **/
interface PlaylistCloudToCacheMapper: Mapper<List<PlaylistItem>,List<PlaylistCache>> {

    class Base @Inject constructor(
        private val mapper: PlaylistItem.Mapper<PlaylistCache>
    ): PlaylistCloudToCacheMapper{

        override fun map(data: List<PlaylistItem>): List<PlaylistCache> {
            return data.map { it.map(mapper) }
        }
    }
}