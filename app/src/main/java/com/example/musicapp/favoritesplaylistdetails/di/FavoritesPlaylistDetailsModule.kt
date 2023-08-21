package com.example.musicapp.favoritesplaylistdetails.di

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.CacheRepository
import com.example.musicapp.app.core.ExtendedGsonConverterFactory
import com.example.musicapp.editplaylist.data.PlaylistDetailsRepository
import com.example.musicapp.editplaylist.data.cache.PlaylistDetailsCacheDataSource
import com.example.musicapp.editplaylist.data.cloud.PlaylistDetailsCloudDataSource
import com.example.musicapp.editplaylist.data.cloud.PlaylistTracksService
import com.example.musicapp.editplaylist.domain.PlaylistDetailsInteractor
import com.example.musicapp.favoritesplaylistdetails.data.cache.TracksCacheToFollowedPlaylistTracksCacheMapper
import com.example.musicapp.main.di.AppModule

import com.example.musicapp.main.di.ViewModelKey
import com.example.musicapp.favoritesplaylistdetails.presentation.HandleFavoritesPlaylistTracksFromCache
import com.example.musicapp.favoritesplaylistdetails.presentation.HandlePlaylistDataCache
import com.example.musicapp.favoritesplaylistdetails.presentation.PlaylistDataCommunication
import com.example.musicapp.favoritesplaylistdetails.presentation.PlaylistDetailsCommunication
import com.example.musicapp.favoritesplaylistdetails.presentation.PlaylistDetailsHandleUiUpdate
import com.example.musicapp.favoritesplaylistdetails.presentation.PlaylistDetailsStateCommunication
import com.example.musicapp.favoritesplaylistdetails.presentation.PlaylistDetailsTracksCommunication
import com.example.musicapp.favoritesplaylistdetails.presentation.FavoritesPlaylistDetailsViewModel
import com.example.musicapp.favoritesplaylistdetails.presentation.PlaylistLoadingCommunication
import com.example.musicapp.userplaylists.domain.PlaylistDomain
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





//    @PlaylistScope
//    @Binds
//    fun bindPlaylistInteractor(obj: PlaylistInteractor.Base):
//            PlaylistInteractor

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

//    @PlaylistScope
//    @Binds
//    fun bindTracksResultToPlaylistTracksCommunicationMapper(obj: TracksResultToPlaylistTracksCommunicationMapper.Base):
//            TracksResultToPlaylistTracksCommunicationMapper

    @Binds
    @[IntoMap ViewModelKey(FavoritesPlaylistDetailsViewModel::class)]
    fun bindQueueViewModel(queueViewModel: FavoritesPlaylistDetailsViewModel): ViewModel
}

@Module
class FavoritesPlaylistDetailsProvidesModule{

//    @Provides
//    @PlaylistsScope
//    fun providePlaylistDomainToIdsMapper(): PlaylistDomain.Mapper<Pair<Int,Int>>{
//        return  PlaylistDomain.ToIdsMapper()
//    }

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