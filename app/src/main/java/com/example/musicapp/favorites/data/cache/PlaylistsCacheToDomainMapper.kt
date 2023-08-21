package com.example.musicapp.favorites.data.cache

import com.example.musicapp.app.core.Mapper
import com.example.musicapp.userplaylists.data.cache.PlaylistCache
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import javax.inject.Inject

/**
 * Created by HP on 13.07.2023.
 **/
interface PlaylistsCacheToDomainMapper: Mapper<List<PlaylistCache>,List<PlaylistDomain>> {

    class Base @Inject constructor(): PlaylistsCacheToDomainMapper
        override fun map(data: List<PlaylistCache>): List<PlaylistDomain> {

            return data.map {
                PlaylistDomain(
                playlistId = it.playlistId,
                title = it.title,
                isFollowing = it.is_following,
                count = it.count,
                description = it.description,
                ownerId = it.owner_id,
                thumbs = it.thumbs
            ) }
        }
    }
