package com.kamancho.melisma.creteplaylist.di

import androidx.lifecycle.ViewModel
import com.kamancho.melisma.addtoplaylist.domain.SelectedTrackDomain
import com.kamancho.melisma.addtoplaylist.domain.SelectedTracksInteractor
import com.kamancho.melisma.addtoplaylist.presentation.SelectedTrackUi
import com.kamancho.melisma.app.core.CacheRepository
import com.kamancho.melisma.app.core.CreatePlaylistInteractor
import com.kamancho.melisma.app.core.EditPlaylistInteractor
import com.kamancho.melisma.app.core.ExtendedGsonConverterFactory
import com.kamancho.melisma.app.core.MusicDatabase
import com.kamancho.melisma.app.core.PlaylistDataInteractor
import com.kamancho.melisma.app.core.TracksCacheToSelectedTracksDomainMapper
import com.kamancho.melisma.app.vkdto.PlaylistItem
import com.kamancho.melisma.creteplaylist.data.PlaylistDataRepository
import com.kamancho.melisma.creteplaylist.data.cache.PlaylistDataCacheDataSource
import com.kamancho.melisma.creteplaylist.data.cloud.AudioIdsToContentIdsMapper
import com.kamancho.melisma.creteplaylist.data.cloud.PlaylistDataCloudDataSource
import com.kamancho.melisma.creteplaylist.data.cloud.PlaylistDataService
import com.kamancho.melisma.creteplaylist.presentation.CreatePlaylistViewModel
import com.kamancho.melisma.creteplaylist.presentation.PlaylistDataResultMapper
import com.kamancho.melisma.creteplaylist.presentation.PlaylistDataUiStateCommunication
import com.kamancho.melisma.creteplaylist.presentation.PlaylistSaveBtnUiStateCommunication
import com.kamancho.melisma.editplaylist.data.PlaylistDetailsRepository
import com.kamancho.melisma.editplaylist.data.cache.PlaylistDetailsCacheDataSource
import com.kamancho.melisma.editplaylist.data.cloud.PlaylistDetailsCloudDataSource
import com.kamancho.melisma.editplaylist.data.cloud.PlaylistTracksService
import com.kamancho.melisma.editplaylist.domain.PlaylistDetailsInteractor
import com.kamancho.melisma.editplaylist.presentation.EditPlaylistViewModel
import com.kamancho.melisma.editplaylist.presentation.EditPlaylistUpdateMapper
import com.kamancho.melisma.editplaylist.presentation.TitleStateCommunication
import com.kamancho.melisma.favorites.data.cache.PlaylistsCacheToDomainMapper
import com.kamancho.melisma.favoritesplaylistdetails.data.cache.TracksCacheToFollowedPlaylistTracksCacheMapper
import com.kamancho.melisma.main.di.AppModule
import com.kamancho.melisma.main.di.ViewModelKey
import com.kamancho.melisma.favoritesplaylistdetails.presentation.HandlePlaylistDataCache
import com.kamancho.melisma.favoritesplaylistdetails.presentation.PlaylistDataCommunication
import com.kamancho.melisma.userplaylists.data.FetchPlaylistsRepository
import com.kamancho.melisma.userplaylists.data.cache.PlaylistCache
import com.kamancho.melisma.userplaylists.data.cache.PlaylistDao
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
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