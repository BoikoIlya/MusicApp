package com.example.musicapp.frienddetails.data

import com.example.musicapp.app.vkdto.SearchPlaylistItem
import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.frienddetails.data.cache.FriendsDetailsCacheDataSource
import com.example.musicapp.frienddetails.data.cloud.FriendsDetailsCloudDataSource
import com.example.musicapp.userplaylists.data.cache.PlaylistCache
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by HP on 18.08.2023.
 **/

interface BaseFriendsDetailsPlaylistsRepository: FriendsDetailsRepository{

class Base @Inject constructor(
    private val cloudDataSource: FriendsDetailsCloudDataSource,
    private val cache: FriendsDetailsCacheDataSource,
    mapper: ModifiedIdToPlaylistCacheMapper
): BaseFriendsDetailsPlaylistsRepository,
    FriendsDetailsRepository.Abstract<SearchPlaylistItem, PlaylistCache>(mapper) {

    override suspend fun cloudData(friendId: String): List<SearchPlaylistItem> =
        cloudDataSource.fetchPlaylists(friendId)

    override suspend fun saveToCache(list: List<PlaylistCache>, friendId: String) =
        cache.insertPlaylists(list, friendId)

}

}