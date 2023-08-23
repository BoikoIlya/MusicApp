package com.kamancho.melisma.creteplaylist.data

import com.kamancho.melisma.app.vkdto.PlaylistItem
import com.kamancho.melisma.creteplaylist.data.cache.PlaylistDataCacheDataSource
import com.kamancho.melisma.creteplaylist.data.cloud.PlaylistDataCloudDataSource
import com.kamancho.melisma.userplaylists.data.cache.PlaylistCache
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 18.07.2023.
 **/
interface PlaylistDataRepository {

    suspend fun createPlaylist(title: String, description: String,audioIds: List<Int>)

    suspend fun followPlaylist(playlist: PlaylistDomain)

    suspend fun editPlaylist(
        playlistId: Int,
        title: String,
        description: String,
        tracksIdsToAdd: List<Int>,
        tracksIdsToDelete: List<Int>,
    )


    suspend fun removeFromPlaylist(playlistId: Int, audioIds: List<Int>)

    class Base @Inject constructor(
        private val cloud: PlaylistDataCloudDataSource,
        private val cache: PlaylistDataCacheDataSource,
        private val mapper: PlaylistItem.Mapper<PlaylistCache>,
        private val playlistDomainToCacheMapper: PlaylistDomain.Mapper<PlaylistCache>
    ): PlaylistDataRepository {

        override suspend fun createPlaylist(title: String, description: String,audioIds: List<Int>) {
            val cloudResult = cloud.createPlaylist(title,description)
            val cacheData = cloudResult.map(mapper)
            cache.insertPlaylist(cacheData)
            cloud.addToPlaylist(cacheData.playlistId,audioIds)
            cache.addTracksToPlaylist(cacheData.playlistId,audioIds)
        }

        override suspend fun followPlaylist(playlist: PlaylistDomain) {
            val playlistCache = playlist.map(playlistDomainToCacheMapper)
            val cloudResult = cloud.followPlaylist(playlistCache.playlistId,playlistCache.owner_id)
            cache.insertPlaylist(cloudResult.map(playlistCache).copy(is_following = true ))
        }

        override suspend fun editPlaylist(
            playlistId: Int,
            title: String,
            description: String,
            tracksIdsToAdd: List<Int>,
            tracksIdsToDelete: List<Int>,
        ) {
            coroutineScope {
                launch {
                    if(!cache.checkIfTitleOrDescriptionDifferent(title, description, playlistId.toString())) return@launch
                    cloud.editPlaylist(playlistId.toString(), title, description)
                    cache.updatePlaylist(playlistId.toString(), title, description)
                }
                launch {
                    cloud.addToPlaylist(playlistId.toString(),tracksIdsToAdd)
                    cache.addTracksToPlaylist(playlistId.toString(),tracksIdsToAdd)
                }
                launch {
                    cloud.removeFromPlaylist(playlistId.toString(),tracksIdsToDelete)
                    cache.removeTracksFromPlaylist(playlistId.toString(),tracksIdsToDelete)
                }
            }
        }


        override suspend fun removeFromPlaylist(playlistId: Int, audioIds: List<Int>) {
            try {
                cache.removeTracksFromPlaylist(playlistId.toString(),audioIds)
                cloud.removeFromPlaylist(playlistId.toString(),audioIds)
            }catch (e: Exception){
                cache.addTracksToPlaylist(playlistId.toString(),audioIds)
                throw e
            }

        }
    }
}