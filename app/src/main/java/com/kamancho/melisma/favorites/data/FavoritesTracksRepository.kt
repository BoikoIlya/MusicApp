package com.kamancho.melisma.favorites.data

import com.kamancho.melisma.app.core.DataTransfer
import com.kamancho.melisma.app.core.FavoritesCacheDataSource
import com.kamancho.melisma.app.core.FavoritesCloudDataSource
import com.kamancho.melisma.app.core.FavoritesRepository
import com.kamancho.melisma.app.core.HandleDeleteRequestData
import com.kamancho.melisma.app.vkdto.TrackItem
import com.kamancho.melisma.favorites.data.cache.DomainToContainsMapper
import com.kamancho.melisma.favorites.data.cache.DomainToDataIdsMapper
import com.kamancho.melisma.favorites.data.cache.TrackCache
import com.kamancho.melisma.favorites.data.cache.TrackDomainToCacheMapper
import com.kamancho.melisma.favorites.data.cloud.TracksCloudToCacheMapper
import com.kamancho.melisma.main.data.cache.AccountDataStore
import com.kamancho.melisma.trending.domain.TrackDomain
import javax.inject.Inject

/**
 * Created by HP on 08.07.2023.
 **/
interface FavoritesTracksRepository: FavoritesRepository<TrackDomain, TrackCache, TrackItem> {


    class Base @Inject constructor(
        transfer: DataTransfer<TrackDomain>,
        domainToContainsMapper: DomainToContainsMapper,
        cloudToCacheMapper: TracksCloudToCacheMapper,
        cloud: FavoritesCloudDataSource<TrackItem>,
        cache: FavoritesCacheDataSource<TrackCache>,
        domainToCacheMapper: TrackDomainToCacheMapper,
        domainToDataIdsMapper: DomainToDataIdsMapper,
        accountDataStore: AccountDataStore,
        handleResponseData: HandleDeleteRequestData<TrackCache>,
    ): FavoritesRepository.Abstract<TrackDomain, TrackCache,TrackItem>(
        domainToCacheMapper,
        domainToDataIdsMapper,
        domainToContainsMapper,
        cloudToCacheMapper,
        transfer,
        accountDataStore,
        handleResponseData,
        cloud,
        cache
    ), FavoritesTracksRepository
}