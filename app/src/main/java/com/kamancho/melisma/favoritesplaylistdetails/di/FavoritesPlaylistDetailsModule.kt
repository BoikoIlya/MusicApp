package com.kamancho.melisma.favoritesplaylistdetails.di

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.CacheRepository
import com.kamancho.melisma.app.core.ExtendedGsonConverterFactory
import com.kamancho.melisma.app.core.PagingSource
import com.kamancho.melisma.editplaylist.data.PlaylistDetailsRepository
import com.kamancho.melisma.editplaylist.data.cache.PlaylistDetailsCacheDataSource
import com.kamancho.melisma.editplaylist.data.cloud.PlaylistDetailsCloudDataSource
import com.kamancho.melisma.editplaylist.data.cloud.PlaylistTracksService
import com.kamancho.melisma.editplaylist.domain.PlaylistDetailsInteractor
import com.kamancho.melisma.favorites.data.cache.TrackCache
import com.kamancho.melisma.favoritesplaylistdetails.data.cache.TracksCacheToFollowedPlaylistTracksCacheMapper
import com.kamancho.melisma.main.di.AppModule

import com.kamancho.melisma.main.di.ViewModelKey
import com.kamancho.melisma.favoritesplaylistdetails.presentation.HandleFavoritesPlaylistTracksFromCache
import com.kamancho.melisma.favoritesplaylistdetails.presentation.HandlePlaylistDataCache
import com.kamancho.melisma.favoritesplaylistdetails.presentation.PlaylistDataCommunication
import com.kamancho.melisma.favoritesplaylistdetails.presentation.PlaylistDetailsCommunication
import com.kamancho.melisma.favoritesplaylistdetails.presentation.PlaylistDetailsHandleUiUpdate
import com.kamancho.melisma.favoritesplaylistdetails.presentation.PlaylistDetailsStateCommunication
import com.kamancho.melisma.favoritesplaylistdetails.presentation.PlaylistDetailsTracksCommunication
import com.kamancho.melisma.favoritesplaylistdetails.presentation.FavoritesPlaylistDetailsViewModel
import com.kamancho.melisma.favoritesplaylistdetails.presentation.PlaylistLoadingCommunication
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Created by HP on 23.04.2023.
 **/
@Module
interface FavoritesPlaylistDetailsModule {

    @Binds
    @FavoritesPlaylistDetailsScope
    fun bindMediaItemsPagingSource(obj: PagingSource.MediaItemsPaging): PagingSource<TrackCache>

    @FavoritesPlaylistDetailsScope
    @Binds
    fun bindTracksCacheToFollowedPlaylistTracksCacheMapper(obj: TracksCacheToFollowedPlaylistTracksCacheMapper.Base):
            TracksCacheToFollowedPlaylistTracksCacheMapper



    @Binds
    @FavoritesPlaylistDetailsScope
    fun bindPlaylistDomainToIdMapper(obj: PlaylistDomain.ToIdMapper): PlaylistDomain.Mapper<String>

    @Binds
    @FavoritesPlaylistDetailsScope
    fun bindPlaylistDomainTitleIsEmptyMapper(obj: PlaylistDomain.TitleIsEmpty): PlaylistDomain.Mapper<Boolean>

    @Binds
    @FavoritesPlaylistDetailsScope
    fun bindHandlePlaylistDataCache(obj: HandlePlaylistDataCache.Base): HandlePlaylistDataCache

    @Binds
    @FavoritesPlaylistDetailsScope
    fun bindPlaylistTracksCloudDataSource(obj: PlaylistDetailsCloudDataSource.Base): PlaylistDetailsCloudDataSource

    @Binds
    @FavoritesPlaylistDetailsScope
    fun bindPlaylistTracksCacheDataSource(obj: PlaylistDetailsCacheDataSource.Base): PlaylistDetailsCacheDataSource.PlaylistDetailsCacheDataSourceFoundById




    @Binds
    @FavoritesPlaylistDetailsScope
    fun bindPlaylistTracksRepository(obj: PlaylistDetailsRepository.Base): PlaylistDetailsRepository

    @Binds
    @FavoritesPlaylistDetailsScope
    fun bindPlaylistTracksInteractor(obj: PlaylistDetailsInteractor.Base): PlaylistDetailsInteractor

    @Binds
    @FavoritesPlaylistDetailsScope
    fun bindCachedTracksRepositoryBaseMediaItem(obj: CacheRepository.BaseMediaItem): CacheRepository<MediaItem>

    @Reusable
    @Binds
    fun bindPlaylistLoadingCommunication(obj: PlaylistLoadingCommunication.Base):
            PlaylistLoadingCommunication

    @FavoritesPlaylistDetailsScope
    @Binds
    fun bindHandleFavoritesPlaylistTracksFromCache(obj: HandleFavoritesPlaylistTracksFromCache.Base):
            HandleFavoritesPlaylistTracksFromCache

    @Reusable
    @Binds
    fun bindPlaylistStateCommunication(obj: PlaylistDetailsStateCommunication.Base):
            PlaylistDetailsStateCommunication

    @Reusable
    @Binds
    fun bindPlaylistDataCommunication(obj: PlaylistDataCommunication.Base):
            PlaylistDataCommunication

    @Reusable
    @Binds
    fun bindPlaylistTracksCommunication(obj: PlaylistDetailsTracksCommunication.Base):
            PlaylistDetailsTracksCommunication

    @FavoritesPlaylistDetailsScope
    @Binds
    fun bindPlaylistCommunication(obj: PlaylistDetailsCommunication.Base):
            PlaylistDetailsCommunication

    @FavoritesPlaylistDetailsScope
    @Binds
    fun bindPlaylistDetailsHandleUiUpdate(obj: PlaylistDetailsHandleUiUpdate.Base):
            PlaylistDetailsHandleUiUpdate


    @Binds
    @[IntoMap ViewModelKey(FavoritesPlaylistDetailsViewModel::class)]
    fun bindQueueViewModel(queueViewModel: FavoritesPlaylistDetailsViewModel): ViewModel
}

@Module
class FavoritesPlaylistDetailsProvidesModule{


    @Provides
    @FavoritesPlaylistDetailsScope
    fun providePlaylistTracksService(
        client: OkHttpClient,
        builder: Retrofit.Builder,
        converterFactory: ExtendedGsonConverterFactory
    ): PlaylistTracksService {
        return builder
            .baseUrl(AppModule.baseDataUrl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(PlaylistTracksService::class.java)
    }
}