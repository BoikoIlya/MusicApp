package com.example.musicapp.trending.data


import com.example.musicapp.R
import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.main.data.cache.AccountDataStore
import com.example.musicapp.trending.data.cloud.TrendingService
import com.example.musicapp.trending.domain.PlaylistDomain
import com.example.musicapp.trending.domain.TrackDomain
import javax.inject.Inject

/**
 * Created by HP on 27.01.2023.
 **/

interface TrendingRepository{

    suspend fun fetchPlaylists(): List<PlaylistDomain>

    suspend fun fetchTracks(): List<TrackDomain>

    class Base @Inject constructor(
        private val service: TrendingService,
        private val toTrackDomain: TrackItem.Mapper<TrackDomain>,
        private val accountData: AccountDataStore,
    ): TrendingRepository {

        companion object{
            private const val tracks_count = 200
        }

        override suspend fun fetchPlaylists(): List<PlaylistDomain> = listOf(
            PlaylistDomain(
                id = "1",
                name = "Random album or playlist",
                imgUrl = "android.resource://com.example.musicapp/" + R.drawable.dance_1,
                tracksUrl = "1"
            ),
//            PlaylistDomain(
//                id = "2",
//                name = "Popular",
//                imgUrl = "android.resource://com.example.musicapp/" + R.drawable.dance_1,
//                tracksUrl = "1"
//            ),
            PlaylistDomain(
                id = "3",
                name = "Playlists",
                imgUrl = "android.resource://com.example.musicapp/" + R.drawable.playlists,
                tracksUrl = "1"
            ),
            PlaylistDomain(
                id = "4",
                name = "Albums",
                imgUrl = "android.resource://com.example.musicapp/" + R.drawable.albums,
                tracksUrl = "1"
            )
        )



        override suspend fun fetchTracks(): List<TrackDomain> =
            service.getRecommendations(accountData.token(),accountData.ownerId(), tracks_count).response.items //todo !!
                .map { it.map(toTrackDomain) }


    }

}