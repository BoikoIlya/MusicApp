package com.example.musicapp.main.di

import com.example.musicapp.app.core.DeleteInteractor
import com.example.musicapp.app.core.FavoritesCacheDataSource
import com.example.musicapp.app.core.FavoritesCloudDataSource
import com.example.musicapp.app.vkdto.PlaylistItem
import com.example.musicapp.userplaylists.data.FavoritesPlaylistsRepository
import com.example.musicapp.userplaylists.data.cache.BaseFavoritesPlaylistsCacheDataSource
import com.example.musicapp.userplaylists.data.cache.PlaylistCache
import com.example.musicapp.userplaylists.data.cloud.BaseFavoritesPlaylistsCloudDataSource
import com.example.musicapp.userplaylists.data.cloud.PlaylistCloudToCacheMapper
import com.example.musicapp.userplaylists.di.PlaylistsScope
import com.example.musicapp.userplaylists.domain.FavoritesPlaylistsInteractor
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import com.example.musicapp.userplaylists.domain.PlaylistDomainToCacheMapper
import com.example.musicapp.userplaylists.domain.PlaylistDomainToContainsMapper
import com.example.musicapp.userplaylists.domain.PlaylistDomainToIdMapper
import com.example.musicapp.userplaylists.domain.PlaylistsResult
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Created by HP on 13.07.2023.
 **/
@Module
interface FavoritesPlaylistsModule {



}