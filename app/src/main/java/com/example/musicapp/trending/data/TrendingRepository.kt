package com.example.musicapp.trending.data


import com.example.musicapp.R
import com.example.musicapp.app.core.ManagerResource
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
        private val captchaDataStore: CaptchaDataStore,
        private val managerResource: ManagerResource
    ): TrendingRepository {

        companion object{
            private const val tracks_count = 200
            const val emptyDestination = 0
            private const val resFolderPath = "android.resource://com.example.musicapp/"
        }

        override suspend fun fetchTopBarItems(): List<TopBarItemDomain> = listOf(
            TopBarItemDomain(
                id = "2",
                title = managerResource.getString(R.string.popular),
                imgUrl = resFolderPath + R.drawable.dance_1,
                onClickNavigationDestination = R.id.action_trendingFragment_to_popularFragment,
                sortPriority = 2
            ),
            TopBarItemDomain(
                id = "1",
                title = managerResource.getString(R.string.tab_playlists_title),
                imgUrl = resFolderPath + R.drawable.popular_music,
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