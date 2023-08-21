package com.example.musicapp.userplaylists.data

import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.FavoritesCacheDataSource
import com.example.musicapp.app.core.FavoritesCloudDataSource
import com.example.musicapp.app.core.FavoritesRepository
import com.example.musicapp.app.core.HandleDeleteRequestData
import com.example.musicapp.app.vkdto.PlaylistItem
import com.example.musicapp.favorites.data.cache.PlaylistsCacheToDomainMapper
import com.example.musicapp.main.data.cache.AccountDataStore
import com.example.musicapp.main.di.AppModule.Companion.mainPlaylistId
import com.example.musicapp.userplaylists.data.cache.BaseFavoritesPlaylistsCacheDataSource
import com.example.musicapp.userplaylists.data.cache.PlaylistCache
import com.example.musicapp.userplaylists.data.cache.PlaylistDao
import com.example.musicapp.userplaylists.data.cloud.BaseFavoritesPlaylistsCloudDataSource
import com.example.musicapp.userplaylists.data.cloud.PlaylistCloudToCacheMapper
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import com.example.musicapp.userplaylists.domain.PlaylistDomainToCacheMapper
import com.example.musicapp.userplaylists.domain.PlaylistDomainToContainsMapper
import com.example.musicapp.userplaylists.domain.PlaylistDomainToIdMapper
import com.example.musicapp.userplaylists.domain.PlaylistDomainToIdMapperInt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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