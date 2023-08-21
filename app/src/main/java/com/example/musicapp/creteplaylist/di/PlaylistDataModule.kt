package com.example.musicapp.creteplaylist.di

import androidx.lifecycle.ViewModel
import com.example.musicapp.addtoplaylist.domain.SelectedTrackDomain
import com.example.musicapp.addtoplaylist.domain.SelectedTracksInteractor
import com.example.musicapp.addtoplaylist.presentation.SelectedTrackUi
import com.example.musicapp.app.core.CacheRepository
import com.example.musicapp.app.core.CreatePlaylistInteractor
import com.example.musicapp.app.core.EditPlaylistInteractor
import com.example.musicapp.app.core.ExtendedGsonConverterFactory
import com.example.musicapp.app.core.MusicDatabase
import com.example.musicapp.app.core.PlaylistDataInteractor
import com.example.musicapp.app.core.TracksCacheToSelectedTracksDomainMapper
import com.example.musicapp.app.vkdto.PlaylistItem
import com.example.musicapp.creteplaylist.data.PlaylistDataRepository
import com.example.musicapp.creteplaylist.data.cache.PlaylistDataCacheDataSource
import com.example.musicapp.creteplaylist.data.cloud.AudioIdsToContentIdsMapper
import com.example.musicapp.creteplaylist.data.cloud.PlaylistDataCloudDataSource
import com.example.musicapp.creteplaylist.data.cloud.PlaylistDataService
import com.example.musicapp.creteplaylist.presentation.CreatePlaylistViewModel
import com.example.musicapp.creteplaylist.presentation.PlaylistDataResultMapper
import com.example.musicapp.creteplaylist.presentation.PlaylistDataUiStateCommunication
import com.example.musicapp.creteplaylist.presentation.PlaylistSaveBtnUiStateCommunication
import com.example.musicapp.editplaylist.data.PlaylistDetailsRepository
import com.example.musicapp.editplaylist.data.cache.PlaylistDetailsCacheDataSource
import com.example.musicapp.editplaylist.data.cloud.PlaylistDetailsCloudDataSource
import com.example.musicapp.editplaylist.data.cloud.PlaylistTracksService
import com.example.musicapp.editplaylist.domain.PlaylistDetailsInteractor
import com.example.musicapp.editplaylist.presentation.EditPlaylistViewModel
import com.example.musicapp.editplaylist.presentation.EditPlaylistUpdateMapper
import com.example.musicapp.editplaylist.presentation.TitleStateCommunication
import com.example.musicapp.favorites.data.cache.PlaylistsCacheToDomainMapper
import com.example.musicapp.favoritesplaylistdetails.data.cache.TracksCacheToFollowedPlaylistTracksCacheMapper
import com.example.musicapp.main.di.AppModule
import com.example.musicapp.main.di.ViewModelKey
import com.example.musicapp.favoritesplaylistdetails.presentation.HandlePlaylistDataCache
import com.example.musicapp.favoritesplaylistdetails.presentation.PlaylistDataCommunication
import com.example.musicapp.userplaylists.data.FetchPlaylistsRepository
import com.example.musicapp.userplaylists.data.cache.PlaylistCache
import com.example.musicapp.userplaylists.data.cache.PlaylistDao
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Created by HP on 16.07.2023.
 **/
@Module
interface PlaylistDataModule {

    @Binds
    @Reusable
    fun bindPlaylistDataUiStateCommunication(obj: PlaylistDataUiStateCommunication.Base): PlaylistDataUiStateCommunication

    @Binds
    @PlaylistDataScope
    fun bindTracksCacheToSelectedTracksDomainMapper(obj: TracksCacheToSelectedTracksDomainMapper.Base): TracksCacheToSelectedTracksDomainMapper

    @Binds
    @PlaylistDataScope
    fun bindSelectedTrackUiToDomain(obj: SelectedTrackUi.ToDomain): SelectedTrackUi.Mapper<SelectedTrackDomain>

    @Binds
    @PlaylistDataScope
    fun bindPlaylistDomainToIdMapper(obj: PlaylistDomain.ToIdMapper): PlaylistDomain.Mapper<String>


    @Binds
    @PlaylistDataScope
    fun bindSelectedTrackDomainToUi(obj: SelectedTrackDomain.ToIdMapper): SelectedTrackDomain.Mapper<Int>

    @Binds
    @PlaylistDataScope
    fun bindPlaylistTracksCacheDataSource(obj: PlaylistDetailsCacheDataSource.Base): PlaylistDetailsCacheDataSource.PlaylistDetailsCacheDataSourceFoundById

    @PlaylistDataScope
    @Binds
    fun bindTracksCacheToFollowedPlaylistTracksCacheMapper(obj: TracksCacheToFollowedPlaylistTracksCacheMapper.Base):
            TracksCacheToFollowedPlaylistTracksCacheMapper

    @Binds
    @PlaylistDataScope
    fun bindPlaylistTracksRepository(obj: PlaylistDetailsRepository.Base): PlaylistDetailsRepository

    @Binds
    @PlaylistDataScope
    fun bindPlaylistTracksInteractor(obj: PlaylistDetailsInteractor.Base): PlaylistDetailsInteractor

    @Binds
    @PlaylistDataScope
    fun bindPlaylistTracksCloudDataSource(obj: PlaylistDetailsCloudDataSource.Base): PlaylistDetailsCloudDataSource

    @Binds
    @PlaylistDataScope
    fun bindToPlaylistUiMapper(obj: PlaylistDomain.ToPlaylistUi): PlaylistDomain.Mapper<PlaylistUi>

    @Binds
    @PlaylistDataScope
    fun providePlaylistCloudPlaylistToCacheMapper(obj: PlaylistItem.ToPlaylistCache): PlaylistItem.Mapper<PlaylistCache>

    @Binds
    @PlaylistDataScope
    fun bindPlaylistDataCacheDataSource(obj: PlaylistDataCacheDataSource.Base): PlaylistDataCacheDataSource

    @Binds
    @PlaylistDataScope
    fun bindPlaylistDataRepository(obj: PlaylistDataRepository.Base): PlaylistDataRepository

    @Binds
    @PlaylistDataScope
    fun bindPlaylistUiPlaylistToDomainMapper(obj: PlaylistUi.ToDomainMapper): PlaylistUi.Mapper<PlaylistDomain>

    @Binds
    @PlaylistDataScope
    fun providePlaylistDomainToPlaylistCacheMapper(obj: PlaylistDomain.ToPlaylistCacheMapper): PlaylistDomain.Mapper<PlaylistCache>

    @Binds
    @PlaylistDataScope
    fun bindAudioIdsToContentIdsMapper(obj: AudioIdsToContentIdsMapper.Base): AudioIdsToContentIdsMapper

    @Binds
    @PlaylistDataScope
    fun bindPlaylistDataCloudDataSource(obj: PlaylistDataCloudDataSource.Base): PlaylistDataCloudDataSource


    @Binds
    @PlaylistDataScope
    fun bindSelectedTracksInteractor(obj: SelectedTracksInteractor.Base): SelectedTracksInteractor

    @Binds
    @PlaylistDataScope
    fun bindCachedTracksRepositoryBaseSelected(obj: CacheRepository.BaseSelected): CacheRepository<SelectedTrackDomain>

    @Binds
    @PlaylistDataScope
    fun bindPlaylistUiToIdMapper(obj: PlaylistUi.ToIdMapper): PlaylistUi.Mapper<String>

    @Binds
    @PlaylistDataScope
    fun bindTitleStateCommunication(obj: TitleStateCommunication.Base): TitleStateCommunication

    @Binds
    @Reusable
    fun bindPlaylistSaveBtnUiStateCommunication(obj: PlaylistSaveBtnUiStateCommunication.Base): PlaylistSaveBtnUiStateCommunication

    @Binds
    @PlaylistDataScope
    fun bindPlaylistResultEditPlaylistUpdateMapper(obj: EditPlaylistUpdateMapper.Base): EditPlaylistUpdateMapper

    @Binds
    @PlaylistDataScope
    fun bindCreatePlaylistInteractor(obj: PlaylistDataInteractor): CreatePlaylistInteractor

    @Binds
    @PlaylistDataScope
    fun bindEditPlaylistInteractor(obj: PlaylistDataInteractor): EditPlaylistInteractor

    @Binds
    @PlaylistDataScope
    fun bindPlaylistDataInteractor(obj: PlaylistDataInteractor.Base): PlaylistDataInteractor

    @Binds
    @PlaylistDataScope
    fun bindPlaylistDataResultMapper(obj: PlaylistDataResultMapper.Base): PlaylistDataResultMapper

    @Reusable
    @Binds
    fun bindPlaylistDataCommunicationn(obj: PlaylistDataCommunication.Base):
            PlaylistDataCommunication

    @Binds
    @PlaylistDataScope
    fun bindHandlePlaylistDataCache(obj: HandlePlaylistDataCache.Base): HandlePlaylistDataCache

    @Binds
    @PlaylistDataScope
    fun bindPlaylistDomainTitleIsEmptyMapper(obj: PlaylistDomain.TitleIsEmpty): PlaylistDomain.Mapper<Boolean>

    @Binds
    @PlaylistDataScope
    fun bindPlaylistsCacheToDomainMapper(obj: PlaylistsCacheToDomainMapper.Base): PlaylistsCacheToDomainMapper

    @Binds
    @PlaylistDataScope
    fun bindFetchPlaylistsRepository(obj: FetchPlaylistsRepository.Base): FetchPlaylistsRepository

    @Binds
    @[IntoMap ViewModelKey(EditPlaylistViewModel::class)]
    fun bindEditPlaylistViewModel(editPlaylistViewModel: EditPlaylistViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(CreatePlaylistViewModel::class)]
    fun bindFavoritesViewModel(createPlaylistViewModel: CreatePlaylistViewModel): ViewModel
}

@Module
class PlaylistDataModuleProvides {

    @Provides
    @PlaylistDataScope
    fun providePlaylistDao(db: MusicDatabase): PlaylistDao {
        return db.getPlaylistDao()
    }

    @Provides
    @PlaylistDataScope
    fun providePlaylistDataService(
        client: OkHttpClient,
        builder: Retrofit.Builder,
        converterFactory: ExtendedGsonConverterFactory
    ): PlaylistDataService {
        return builder
            .baseUrl(AppModule.baseDataUrl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(PlaylistDataService::class.java)
    }



    @Provides
    @PlaylistDataScope
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

    @Provides
    @PlaylistDataScope
    fun providePlaylistDomainToIdsMapper(): PlaylistDomain.Mapper<Pair<Int,String>>{
        return  PlaylistDomain.ToIdsMapper()
    }
}