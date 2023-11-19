package com.kamancho.melisma.trending.data


import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.ManagerResource
import com.kamancho.melisma.app.vkdto.SearchPlaylistItem
import com.kamancho.melisma.app.vkdto.TrackItem
import com.kamancho.melisma.captcha.data.cache.CaptchaDataStore
import com.kamancho.melisma.main.data.cache.AccountDataStore
import com.kamancho.melisma.trending.data.cloud.TrendingService
import com.kamancho.melisma.trending.domain.TopBarItemDomain
import com.kamancho.melisma.trending.domain.TrackDomain
import com.kamancho.melisma.trending.domain.TrendingNavigationState
import com.kamancho.melisma.trending.presentation.TrendingTopBarNavigationState
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import javax.inject.Inject

/**
 * Created by HP on 27.01.2023.
 **/

interface TrendingRepository {

    suspend fun fetchTopBarItems(): List<TopBarItemDomain>

    suspend fun fetchTracks(): List<TrackDomain>

    suspend fun fetchEmbeddedVkPlaylists(): List<TopBarItemDomain>

    class Base @Inject constructor(
        private val service: TrendingService,
        private val toTrackDomain: TrackItem.Mapper<TrackDomain>,
        private val accountData: AccountDataStore,
        private val captchaDataStore: CaptchaDataStore,
        private val managerResource: ManagerResource,
    ) : TrendingRepository {

        companion object {
            private const val tracks_count = 200
            private const val resFolderPath = "android.resource://com.kamancho.melisma/"
        }

        override suspend fun fetchTopBarItems(): List<TopBarItemDomain> = listOf(
            TopBarItemDomain(
                id = "2",
                title = managerResource.getString(R.string.popular),
                imgUrl = resFolderPath + R.drawable.dance_1,
                onClickNavigationDestination = TrendingNavigationState.SimpleNavigation(R.id.action_trendingFragment_to_popularFragment),
                sortPriority = 2
            ),
            TopBarItemDomain(
                id = "1",
                title = managerResource.getString(R.string.favorites_playlists),
                imgUrl = resFolderPath + R.drawable.popular_music,
                onClickNavigationDestination = TrendingNavigationState.SimpleNavigation(R.id.action_trendingFragment_to_playlistsFragment),
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

        override suspend fun fetchEmbeddedVkPlaylists(): List<TopBarItemDomain> {
            val ownerId = accountData.ownerId().toInt()
            return listOf(
                TopBarItemDomain(
                    "-21",
                    managerResource.getString(R.string.for_you),
                    "https://sun25-2.userapi.com/impg/qi-uU9djK_0vNFJP2cIuOLcRRtMSazFIKPYNqA/m2gRCS5SmRA.jpg?size=320x320&quality=95&crop=0,0,594,594&sign=7d3b767db421e2fa13064c600070dc57&type=audio",
                    TrendingNavigationState.EmbeddedVkPlaylistNavigation(
                        PlaylistDomain(
                            playlistId = "-21",
                            title = managerResource.getString(R.string.for_you),
                            isFollowing = false,
                            count = 0,
                            description = "",
                            ownerId = ownerId,
                            thumbs = listOf(
                                "https://sun25-2.userapi.com/impg/qi-uU9djK_0vNFJP2cIuOLcRRtMSazFIKPYNqA/m2gRCS5SmRA.jpg?size=320x320&quality=95&crop=0,0,594,594&sign=7d3b767db421e2fa13064c600070dc57&type=audio"
                            )
                        )
                    )
                ),
                TopBarItemDomain(
                    "-24",
                    managerResource.getString(R.string.discoveries),
                    "https://sun25-2.userapi.com/impg/FSkK6XzOP2Hf4Gaz42KXmHyQGI4flzePi7QCFg/paJJbA8zteQ.jpg?size=320x320&quality=95&crop=0,0,594,594&sign=ed0f94313aa71e2227eba89ff6a0ffce&type=audio",
                    TrendingNavigationState.EmbeddedVkPlaylistNavigation(
                        PlaylistDomain(
                            playlistId = "-24",
                            title = managerResource.getString(R.string.discoveries),
                            isFollowing = false,
                            count = 0,
                            description = "",
                            ownerId = ownerId,
                            thumbs = listOf(
                                "https://sun25-2.userapi.com/impg/FSkK6XzOP2Hf4Gaz42KXmHyQGI4flzePi7QCFg/paJJbA8zteQ.jpg?size=320x320&quality=95&crop=0,0,594,594&sign=ed0f94313aa71e2227eba89ff6a0ffce&type=audio",
                            )
                        )
                    )
                ),
                TopBarItemDomain(
                    "-23",
                    managerResource.getString(R.string.new_playlists),
                    "https://sun25-2.userapi.com/impg/84DQUzUa3dMM0a6TgbPupRCwOC3wwF5qPZ84hA/i8T_1-ty-eI.jpg?size=320x320&quality=95&crop=0,0,594,594&sign=dcc77c75c155b346827ce1c4e40c8b98&type=audio",
                    TrendingNavigationState.EmbeddedVkPlaylistNavigation(
                        PlaylistDomain(
                            playlistId = "-23",
                            title = managerResource.getString(R.string.new_playlists),
                            isFollowing = false,
                            count = 0,
                            description = "",
                            ownerId = ownerId,
                            thumbs = listOf(
                                "https://sun25-2.userapi.com/impg/84DQUzUa3dMM0a6TgbPupRCwOC3wwF5qPZ84hA/i8T_1-ty-eI.jpg?size=320x320&quality=95&crop=0,0,594,594&sign=dcc77c75c155b346827ce1c4e40c8b98&type=audio",
                            )
                        )
                    )
                ),
                TopBarItemDomain(
                    "-25",
                    managerResource.getString(R.string.playlists_1_day),
                    "https://sun25-2.userapi.com/impg/PE9lX3syz9eKJEnBK0aZ9ur4byLjwbxxgWpydg/MzgfyxRsBK0.jpg?size=320x320&quality=95&crop=0,0,594,594&sign=e476f02915f4c7a854fa800238ff08ef&type=audio",
                    TrendingNavigationState.EmbeddedVkPlaylistNavigation(
                        PlaylistDomain(
                            playlistId = "-25",
                            title = managerResource.getString(R.string.playlists_1_day),
                            isFollowing = false,
                            count = 0,
                            description = "",
                            ownerId = ownerId,
                            thumbs = listOf(
                                "https://sun25-2.userapi.com/impg/PE9lX3syz9eKJEnBK0aZ9ur4byLjwbxxgWpydg/MzgfyxRsBK0.jpg?size=320x320&quality=95&crop=0,0,594,594&sign=e476f02915f4c7a854fa800238ff08ef&type=audio",
                            )
                        )
                    )
                ),
                TopBarItemDomain(
                    "-26",
                    managerResource.getString(R.string.playlists_2_day),
                    "https://sun25-2.userapi.com/impg/_xFDD4zOMk8ln0ZALiDu9uz0sIsBNBSuMymmsQ/HL7hA0lSB2w.jpg?size=320x320&quality=95&crop=0,0,594,594&sign=2869e1dff7f134affe24da91854777d7&type=audio",
                    TrendingNavigationState.EmbeddedVkPlaylistNavigation(
                        PlaylistDomain(
                            playlistId = "-26",
                            title = managerResource.getString(R.string.playlists_2_day),
                            isFollowing = false,
                            count = 0,
                            description = "",
                            ownerId = ownerId,
                            thumbs = listOf(
                                "https://sun25-2.userapi.com/impg/_xFDD4zOMk8ln0ZALiDu9uz0sIsBNBSuMymmsQ/HL7hA0lSB2w.jpg?size=320x320&quality=95&crop=0,0,594,594&sign=2869e1dff7f134affe24da91854777d7&type=audio",
                            )
                        )
                    )
                ),
                TopBarItemDomain(
                    "-27",
                    managerResource.getString(R.string.playlists_3_day),
                    "https://sun25-2.userapi.com/impg/JNxN4nD8zIrrwMw_QBxZofWZjnmGhQmTHEukGA/JjGocmtLwsM.jpg?size=320x320&quality=95&crop=0,0,594,594&sign=074182b100656af0c2e699065751b0b2&type=audio",
                    TrendingNavigationState.EmbeddedVkPlaylistNavigation(
                        PlaylistDomain(
                            playlistId = "-27",
                            title = managerResource.getString(R.string.playlists_3_day),
                            isFollowing = false,
                            count = 0,
                            description = "",
                            ownerId = ownerId,
                            thumbs = listOf(
                                "https://sun25-2.userapi.com/impg/JNxN4nD8zIrrwMw_QBxZofWZjnmGhQmTHEukGA/JjGocmtLwsM.jpg?size=320x320&quality=95&crop=0,0,594,594&sign=074182b100656af0c2e699065751b0b2&type=audio",
                            )
                        )
                    )
                ),
                TopBarItemDomain(
                    "-22",
                    managerResource.getString(R.string.playlists_of_week),
                    "https://sun25-1.userapi.com/impg/mV9UOJeoHJh5NFKI4JSxY9SpfHBWhZh8RqTAAg/PoTuIgyNGSU.jpg?size=320x320&quality=95&crop=0,0,594,594&sign=4f165ba0e3621a66df4dca66b4b2a6ab&type=audio",
                    TrendingNavigationState.EmbeddedVkPlaylistNavigation(
                        PlaylistDomain(
                            playlistId = "-22",
                            title = managerResource.getString(R.string.playlists_of_week),
                            isFollowing = false,
                            count = 0,
                            description = "",
                            ownerId = ownerId,
                            thumbs = listOf(
                                "https://sun25-1.userapi.com/impg/mV9UOJeoHJh5NFKI4JSxY9SpfHBWhZh8RqTAAg/PoTuIgyNGSU.jpg?size=320x320&quality=95&crop=0,0,594,594&sign=4f165ba0e3621a66df4dca66b4b2a6ab&type=audio"
                            )
                        )
                    )
                )
            )
        }

    }

}
