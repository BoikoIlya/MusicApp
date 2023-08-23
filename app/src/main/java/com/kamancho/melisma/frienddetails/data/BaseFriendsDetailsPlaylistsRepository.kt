package com.kamancho.melisma.frienddetails.data

import com.kamancho.melisma.app.vkdto.SearchPlaylistItem
import com.kamancho.melisma.frienddetails.data.cache.FriendsDetailsCacheDataSource
import com.kamancho.melisma.frienddetails.data.cloud.FriendsDetailsCloudDataSource
import com.kamancho.melisma.userplaylists.data.cache.PlaylistCache
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
        cloudDataSource.fetchPlaylists(friendId).filter { !it.isBlocked() }

    override suspend fun saveToCache(list: List<PlaylistCache>, friendId: String) =
        cache.insertPlaylists(list, friendId)

}

}