package com.example.musicapp.trending.data


import com.example.musicapp.R
import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.captcha.data.cache.CaptchaDataStore
import com.example.musicapp.main.data.cache.AccountDataStore
import com.example.musicapp.trending.data.cloud.TrendingService
import com.example.musicapp.trending.domain.TopBarItemDomain
import com.example.musicapp.trending.domain.TrackDomain
import javax.inject.Inject

/**
 * Created by HP on 27.01.2023.
 **/

interface TrendingRepository{

    suspend fun fetchTopBarItems(): List<TopBarItemDomain>

    suspend fun fetchTracks(): List<TrackDomain>

    class Base @Inject constructor(
        private val service: TrendingService,
        private val toTrackDomain: TrackItem.Mapper<TrackDomain>,
        private val accountData: AccountDataStore,
        private val captchaDataStore: CaptchaDataStore
    ): TrendingRepository {

        companion object{
            private const val tracks_count = 200
            const val emptyDestination = 0
        }

        override suspend fun fetchTopBarItems(): List<TopBarItemDomain> = listOf(
//            PlaylistDomain(
//                id = "2",
//                name = "Popular",
//                imgUrl = "android.resource://com.example.musicapp/" + R.drawable.dance_1,
//                tracksUrl = "1"
//            ),
//            TopBarItemDomain(
//                id = "3",
//                title = "Popular",
//                imgUrl = "android.resource://com.example.musicapp/" + R.drawable.playlists,
//                onClickNavigationDestination = emptyDestination,
//                sortPriority = 3
//            ),
//            TopBarItemDomain(
//                id = "4",
//                title = "Albums",
//                imgUrl = "android.resource://com.example.musicapp/" + R.drawable.albums,
//                onClickNavigationDestination = emptyDestination,
//                sortPriority = 3
//            ),
            TopBarItemDomain(
                id = "1",
                title = "Playlists",
                imgUrl = "android.resource://com.example.musicapp/" + R.drawable.dance_1,
                onClickNavigationDestination = R.id.action_trendingFragment_to_playlistsFragment,
                sortPriority = 1
            ),
        )



        override suspend fun fetchTracks(): List<TrackDomain> =
            service.getRecommendations(
                accountData.token(),
                accountData.ownerId(),
                tracks_count,
                captchaDataStore.captchaId(),
                captchaDataStore.captchaEnteredData()
            ).response.items
                .map { it.map(toTrackDomain) }


    }

}