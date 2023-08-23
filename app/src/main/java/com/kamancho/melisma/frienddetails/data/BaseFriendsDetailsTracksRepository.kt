package com.kamancho.melisma.frienddetails.data

import com.kamancho.melisma.app.vkdto.TrackItem
import com.kamancho.melisma.favorites.data.cache.TrackCache
import com.kamancho.melisma.frienddetails.data.cache.FriendsDetailsCacheDataSource
import com.kamancho.melisma.frienddetails.data.cloud.FriendsDetailsCloudDataSource
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