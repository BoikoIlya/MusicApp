package com.example.musicapp.frienddetails.data

import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.frienddetails.data.cache.FriendsDetailsCacheDataSource
import com.example.musicapp.frienddetails.data.cloud.FriendsDetailsCloudDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by HP on 18.08.2023.
 **/
interface BaseFriendsDetailsTracksRepository: FriendsDetailsRepository{

class Base @Inject constructor(
    private val cloudDataSource: FriendsDetailsCloudDataSource,
    private val cache: FriendsDetailsCacheDataSource,
    mapper: ModifiedIdToTrackCacheMapper
): BaseFriendsDetailsTracksRepository,
    FriendsDetailsRepository.Abstract<TrackItem, TrackCache>(mapper) {

    override suspend fun cloudData(friendId: String): List<TrackItem> =
        cloudDataSource.fetchTracks(friendId)

    override suspend fun saveToCache(list: List<TrackCache>, friendId: String) =
        cache.insertTracks(list, friendId)


}
}