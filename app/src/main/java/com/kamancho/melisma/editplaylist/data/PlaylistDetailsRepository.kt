package com.kamancho.melisma.editplaylist.data

import android.util.Log
import com.kamancho.melisma.app.vkdto.PlaylistItem
import com.kamancho.melisma.editplaylist.data.cache.PlaylistDetailsCacheDataSource
import com.kamancho.melisma.editplaylist.data.cloud.PlaylistDetailsCloudDataSource
import com.kamancho.melisma.favorites.data.cloud.TracksCloudToCacheMapper
import com.kamancho.melisma.favoritesplaylistdetails.data.cache.TracksCacheToFollowedPlaylistTracksCacheMapper
import com.kamancho.melisma.userplaylists.data.cache.PlaylistCache
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 18.07.2023.
 **/
interface PlaylistDetailsRepository {

    suspend fun fetch(playlistId: String,ownerId: Int)

    class Base @Inject constructor(
        private val cloud: PlaylistDetailsCloudDataSource,
        private val cache: PlaylistDetailsCacheDataSource.PlaylistDetailsCacheDataSourceFoundById,
        private val tracksCloudToCacheMapper: TracksCloudToCacheMapper,
        private val playlistCloudToCacheMapper: PlaylistItem.Mapper<PlaylistCache>,
        private val tracksCacheToFollowedPlaylistTracksCacheMapper: TracksCacheToFollowedPlaylistTracksCacheMapper
    ): PlaylistDetailsRepository {

        override suspend fun fetch(playlistId: String,ownerId: Int) {
            coroutineScope {
                launch {
                    val cloudResult = cloud.fetchTracks(playlistId,ownerId)
                    Log.d("tag", "fetch: ${cloudResult.size} ")
                    val playlistItem = cache.getPlaylistById(playlistId)
                    cache.savePlaylistTracks(
                        playlistId,
                      tracksCacheToFollowedPlaylistTracksCacheMapper.map(
                          Pair(playlistItem,tracksCloudToCacheMapper.map(cloudResult))
                      )
                    )
                }
                launch {
                    val cloudResult = cloud.fetchFavoritesPlaylistById(playlistId,ownerId)
                    cache.updatePlaylistData(playlistId,cloudResult.map(playlistCloudToCacheMapper))
                }
            }
        }
    }
}