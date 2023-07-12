package com.example.musicapp.favorites.data

import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.FavoritesCacheDataSource
import com.example.musicapp.app.core.FavoritesCloudDataSource
import com.example.musicapp.app.core.FavoritesRepository
import com.example.musicapp.app.core.HandleDeleteRequestData
import com.example.musicapp.app.core.HandleDeleteTrackRequest
import com.example.musicapp.app.core.ToMediaItemMapper
import com.example.musicapp.app.vkdto.Item
import com.example.musicapp.favorites.data.cache.DomainToContainsMapper
import com.example.musicapp.favorites.data.cache.DomainToDataIdsMapper
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.favorites.data.cache.TrackDomainToCacheMapper
import com.example.musicapp.favorites.data.cache.TracksDao
import com.example.musicapp.favorites.data.cloud.TracksCloudToCacheMapper
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.main.data.cache.AccountDataStore
import com.example.musicapp.trending.domain.TrackDomain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by HP on 08.07.2023.
 **/
interface FavoritesTracksRepository: FavoritesRepository<TrackDomain, TrackCache, Item> {

    fun fetchData(state: SortingState): Flow<TracksResult>

    class Base @Inject constructor(
        private val dao: TracksDao,
        private val toMediaItemMapper: ToMediaItemMapper,
        transfer: DataTransfer<TrackDomain>,
        domainToContainsMapper: DomainToContainsMapper,
        cloudToCacheMapper: TracksCloudToCacheMapper,
        cloud: FavoritesCloudDataSource<Item>,
        cache: FavoritesCacheDataSource<TrackCache>,
        domainToCacheMapper: TrackDomainToCacheMapper,
        domainToDataIdsMapper: DomainToDataIdsMapper,
        accountDataStore: AccountDataStore,
        handleResponseData: HandleDeleteRequestData<TrackCache>,
    ): FavoritesRepository.Abstract<TrackDomain, TrackCache,Item>(
        domainToCacheMapper,
        domainToDataIdsMapper,
        domainToContainsMapper,
        cloudToCacheMapper,
        transfer,
        accountDataStore,
        handleResponseData,
        cloud,
        cache
    ), FavoritesTracksRepository {

        override fun fetchData(state: SortingState): Flow<TracksResult> =
            state.fetch(dao,toMediaItemMapper,TrackCache.currentAccountAllMusicPlaylistId)

    }
}