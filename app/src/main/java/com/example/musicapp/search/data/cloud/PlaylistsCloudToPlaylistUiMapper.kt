package com.example.musicapp.search.data.cloud

import android.util.Log
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.Mapper
import com.example.musicapp.app.vkdto.PlaylistItem
import com.example.musicapp.app.vkdto.SearchPlaylistItem
import com.example.musicapp.app.vkdto.SearchPlaylistsCloud
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 14.08.2023.
 **/
interface PlaylistsCloudToPlaylistUiMapper: Mapper<SearchPlaylistsCloud,List<PlaylistUi>> {

    class Base @Inject constructor(
        private val mapper: SearchPlaylistItem.Mapper<PlaylistUi>
    ): PlaylistsCloudToPlaylistUiMapper {

        override fun map(data: SearchPlaylistsCloud): List<PlaylistUi> {
            return data.response.items.map { it.map(mapper) }
        }
    }
}