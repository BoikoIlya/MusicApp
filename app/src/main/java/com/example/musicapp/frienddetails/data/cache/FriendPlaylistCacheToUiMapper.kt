package com.example.musicapp.frienddetails.data.cache

import com.example.musicapp.app.core.Mapper
import com.example.musicapp.app.vkdto.SearchPlaylistItem.Companion.amount_of_images_need
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

            return data.map {
                val stateList = emptyList<PlaylistThumbsState>().toMutableList()
                stateList.addAll(it.thumbs.map {thumb-> PlaylistThumbsState.LoadImages(thumb) })

                while (stateList.size<amount_of_images_need){
                    stateList.add(PlaylistThumbsState.Empty)
                }

                PlaylistUi(
                playlistId = it.playlistId,
                title = it.title,
                isFollowing = it.is_following,
                count = it.count,
                description = it.description,
                ownerId = it.owner_id,
                thumbStates = stateList
            ) }
        }
    }
}