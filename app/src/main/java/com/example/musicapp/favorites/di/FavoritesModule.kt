package com.example.musicapp.favorites.di

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.*
import com.example.musicapp.favorites.data.FavoriteTracksRepository
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.favorites.presentation.*
import com.example.musicapp.main.di.ViewModelKey
import com.example.musicapp.trending.presentation.TrendingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by HP on 21.03.2023.
 **/
@Module
interface FavoritesModule {

    @FavoritesScope
    @Binds
    fun bindFavoritesCommunication(obj: FavoritesCommunication.Base): FavoritesCommunication

    @FavoritesScope
    @Binds
    fun bindFavoritesStateCommunication(obj: FavoritesStateCommunication.Base): FavoritesStateCommunication

    @FavoritesScope
    @Binds
    fun bindFavoritesTrackListCommunication(obj: FavoritesTrackListCommunication.Base): FavoritesTrackListCommunication

    @FavoritesScope
    @Binds
    fun bindTracksResultToFavoriteTracksCommunicationMapper(obj: TracksResultToFavoriteTracksCommunicationMapper.Base): TracksResultToFavoriteTracksCommunicationMapper

    @Binds
    @[IntoMap ViewModelKey(FavoritesViewModel::class)]
    fun bindTrendingViewModel(favoritesViewModel: FavoritesViewModel): ViewModel
}