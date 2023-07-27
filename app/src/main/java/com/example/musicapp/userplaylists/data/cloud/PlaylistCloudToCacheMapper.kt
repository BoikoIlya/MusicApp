package com.example.musicapp.userplaylists.data.cloud

import android.util.Log
import com.example.musicapp.app.core.Mapper
import com.example.musicapp.app.vkdto.PlaylistItem
import com.example.musicapp.userplaylists.data.cache.PlaylistCache
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