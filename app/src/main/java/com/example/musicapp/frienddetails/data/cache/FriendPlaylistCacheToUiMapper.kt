package com.example.musicapp.frienddetails.data.cache

import com.example.musicapp.app.core.Mapper
import com.example.musicapp.userplaylists.data.cache.PlaylistCache
import com.example.musicapp.userplaylists.presentation.PlaylistThumbsState
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 19.08.2023.
 **/
interface FriendPlaylistsCacheToUiMapper: Mapper<List<PlaylistCache>, List<PlaylistUi>> {

    class Base @Inject constructor(): FriendPlaylistsCacheToUiMapper {
        override fun map(data: List<PlaylistCache>): List<PlaylistUi> {
            return data.map { PlaylistUi(
                playlistId = it.playlistId,
                title = it.title,
                isFollowing = it.is_following,
                count = it.count,
                description = it.description,
                ownerId = it.owner_id,
                thumbStates = listOf(
                    PlaylistThumbsState.LoadImages(it.thumbs.first()),
                    PlaylistThumbsState.Empty,
                    PlaylistThumbsState.Empty,
                    PlaylistThumbsState.Empty,
                    )
            ) }
        }
    }
}