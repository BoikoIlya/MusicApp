package com.kamancho.melisma.search.data.cloud

import com.kamancho.melisma.app.core.Mapper
import com.kamancho.melisma.app.vkdto.SearchPlaylistItem
import com.kamancho.melisma.app.vkdto.SearchPlaylistsCloud
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
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