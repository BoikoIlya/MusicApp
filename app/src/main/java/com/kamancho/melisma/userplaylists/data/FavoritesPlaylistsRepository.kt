package com.kamancho.melisma.userplaylists.data

import com.kamancho.melisma.app.core.DataTransfer
import com.kamancho.melisma.app.core.FavoritesCacheDataSource
import com.kamancho.melisma.app.core.FavoritesCloudDataSource
import com.kamancho.melisma.app.core.FavoritesRepository
import com.kamancho.melisma.app.core.HandleDeleteRequestData
import com.kamancho.melisma.app.vkdto.PlaylistItem
import com.kamancho.melisma.main.data.cache.AccountDataStore
import com.kamancho.melisma.userplaylists.data.cache.PlaylistCache
import com.kamancho.melisma.userplaylists.data.cloud.PlaylistCloudToCacheMapper
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import com.kamancho.melisma.userplaylists.domain.PlaylistDomainToCacheMapper
import com.kamancho.melisma.userplaylists.domain.PlaylistDomainToContainsMapper
import com.kamancho.melisma.userplaylists.domain.PlaylistDomainToIdMapperInt
import javax.inject.Inject

/**
 * Created by HP on 12.07.2023.
 **/
interface FavoritesPlaylistsRepository: FavoritesRepository<PlaylistDomain,PlaylistCache,PlaylistItem> {


    class Base @Inject constructor(
        playlistDomainToCacheMapper: PlaylistDomainToCacheMapper,
        playlistDomainToIdMapper: PlaylistDomainToIdMapperInt,
        playlistDomainToContainsMapper: PlaylistDomainToContainsMapper,
        playlistCloudToCacheMapper: PlaylistCloudToCacheMapper,
        transfer: DataTransfer<PlaylistDomain>,
        accountDataStore: AccountDataStore,
        handleResponseData: HandleDeleteRequestData<PlaylistCache>,
        favoritesPlaylistsCloudDataSource: FavoritesCloudDataSource<PlaylistItem>,
        favoritesPlaylistsCacheDataSource: FavoritesCacheDataSource<PlaylistCache>,
    ): FavoritesPlaylistsRepository,
        FavoritesRepository.Abstract<PlaylistDomain,PlaylistCache,PlaylistItem>(
            playlistDomainToCacheMapper,
            playlistDomainToIdMapper,
            playlistDomainToContainsMapper,
            playlistCloudToCacheMapper,
            transfer,
            accountDataStore,
            handleResponseData,
            favoritesPlaylistsCloudDataSource,
            favoritesPlaylistsCacheDataSource
        ) {
    }
}