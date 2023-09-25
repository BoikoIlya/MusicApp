package com.kamancho.melisma.trending.data


import android.util.Log
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.ManagerResource
import com.kamancho.melisma.app.core.PagingSource
import com.kamancho.melisma.app.vkdto.TrackItem
import com.kamancho.melisma.captcha.data.cache.CaptchaDataStore
import com.kamancho.melisma.main.data.cache.AccountDataStore
import com.kamancho.melisma.trending.data.cloud.TrendingService
import com.kamancho.melisma.trending.domain.TopBarItemDomain
import com.kamancho.melisma.trending.domain.TrackDomain
import javax.inject.Inject

/**
 * Created by HP on 27.01.2023.
 **/

interface TrendingRepository{

    suspend fun fetchTopBarItems(): List<TopBarItemDomain>

    suspend fun fetchTracks(): List<TrackDomain>

    fun resetOffset()

    class Base @Inject constructor(
        private val service: TrendingService,
        private val toTrackDomain: TrackItem.Mapper<TrackDomain>,
        private val accountData: AccountDataStore,
        private val captchaDataStore: CaptchaDataStore,
        private val managerResource: ManagerResource,
        private val pagingSource: PagingSource<TrackDomain>
    ): TrendingRepository {

        companion object{
            const val emptyDestination = 0
            private const val resFolderPath = "android.resource://com.kamancho.melisma/"
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
            pagingSource.newPage { offset, pageSize ->
                Log.d("tag", "fetchTracks: $offset $pageSize ")
                service.getRecommendations(
                    accountData.token(),
                    accountData.ownerId(),
                    offset,
                    pageSize,
                    captchaDataStore.captchaId(),
                    captchaDataStore.captchaEnteredData()
                ).response.items
                    .map { it.map(toTrackDomain) }

            }

        override fun resetOffset() = pagingSource.resetOffset()


    }

}