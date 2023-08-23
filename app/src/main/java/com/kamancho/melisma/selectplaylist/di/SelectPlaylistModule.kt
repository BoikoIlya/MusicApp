package com.kamancho.melisma.selectplaylist.di

import androidx.lifecycle.ViewModel
import com.kamancho.melisma.app.core.ExtendedGsonConverterFactory
import com.kamancho.melisma.app.core.FavoritesCacheDataSource
import com.kamancho.melisma.app.core.FavoritesCloudDataSource
import com.kamancho.melisma.app.core.HandleDeletePlaylistRequest
import com.kamancho.melisma.app.core.HandleDeleteRequestData
import com.kamancho.melisma.app.core.MusicDatabase
import com.kamancho.melisma.app.vkdto.PlaylistItem
import com.kamancho.melisma.creteplaylist.data.cache.PlaylistDataCacheDataSource
import com.kamancho.melisma.creteplaylist.data.cloud.AudioIdsToContentIdsMapper
import com.kamancho.melisma.creteplaylist.data.cloud.PlaylistDataCloudDataSource
import com.kamancho.melisma.creteplaylist.data.cloud.PlaylistDataService
import com.kamancho.melisma.favorites.data.cache.PlaylistsCacheToDomainMapper
import com.kamancho.melisma.favorites.presentation.FavoritesTracksLoadingCommunication
import com.kamancho.melisma.main.di.AppModule.Companion.baseDataUrl
import com.kamancho.melisma.main.di.ViewModelKey
import com.kamancho.melisma.selectplaylist.data.AddTrackToPlaylistRepository
import com.kamancho.melisma.selectplaylist.domain.AddTrackToPlaylistInteractor
import com.kamancho.melisma.selectplaylist.presentation.CachedPlaylistsCommunication
import com.kamancho.melisma.selectplaylist.presentation.HandleFavoritesPlaylistsFromCache
import com.kamancho.melisma.selectplaylist.presentation.SelectPlaylistCommunication
import com.kamancho.melisma.selectplaylist.presentation.SelectPlaylistUiStateCommunication
import com.kamancho.melisma.selectplaylist.presentation.SelectPlaylistViewModel
import com.kamancho.melisma.userplaylists.data.FavoritesPlaylistsRepository
import com.kamancho.melisma.userplaylists.data.FetchPlaylistsRepository
import com.kamancho.melisma.userplaylists.data.cache.BaseFavoritesPlaylistsCacheDataSource
import com.kamancho.melisma.userplaylists.data.cache.PlaylistCache
import com.kamancho.melisma.userplaylists.data.cache.PlaylistDao
import com.kamancho.melisma.userplaylists.data.cloud.BaseFavoritesPlaylistsCloudDataSource
import com.kamancho.melisma.userplaylists.data.cloud.PlaylistCloudToCacheMapper
import com.kamancho.melisma.userplaylists.data.cloud.PlaylistsService
import com.kamancho.melisma.userplaylists.domain.FavoritesPlaylistsInteractor
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import com.kamancho.melisma.userplaylists.domain.PlaylistDomainToCacheMapper
import com.kamancho.melisma.userplaylists.domain.PlaylistDomainToContainsMapper
import com.kamancho.melisma.userplaylists.domain.PlaylistDomainToIdMapper
import com.kamancho.melisma.userplaylists.domain.PlaylistDomainToIdMapperInt
import com.kamancho.melisma.userplaylists.domain.PlaylistUiToDomainMapper
import com.kamancho.melisma.userplaylists.presentation.FavoritesPlaylistCommunication
import com.kamancho.melisma.userplaylists.presentation.FavoritesPlaylistsUiCommunication
import com.kamancho.melisma.userplaylists.presentation.HandleFavoritesPlaylistsUiUpdate
import com.kamancho.melisma.userplaylists.presentation.HandlePlaylistsFetchingFromCache
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import com.kamancho.melisma.userplaylists.presentation.PlaylistsResultUpdateToUiEventMapper
import com.kamancho.melisma.userplaylists.presentation.PlaylistsUiStateCommunication
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Created by HP on 01.05.2023.
 **/
@Module
interface SelectPlaylistModule {



    @Binds
    @SelectPlaylistScope
    fun bindPlaylistsCacheToDomainMapper(obj: PlaylistsCacheToDomainMapper.Base): PlaylistsCacheToDomainMapper

    @Binds
    @SelectPlaylistScope
    fun bindPlaylistUiPlaylistToDomainMapper(obj: PlaylistUi.ToDomainMapper): PlaylistUi.Mapper<PlaylistDomain>

    @Binds
    @SelectPlaylistScope
    fun bindToPlaylistUiMapper(obj: PlaylistDomain.ToPlaylistUi): PlaylistDomain.Mapper<PlaylistUi>

    @Binds
    @SelectPlaylistScope
    fun bindPlaylistUiToDomainMapper(obj: PlaylistUiToDomainMapper.Base): PlaylistUiToDomainMapper

    @Binds
    @SelectPlaylistScope
    fun providePlaylistCloudToCacheMapper(obj: PlaylistCloudToCacheMapper.Base): PlaylistCloudToCacheMapper

    @Binds
    @SelectPlaylistScope
    fun providePlaylistCloudPlaylistToCacheMapper(obj: PlaylistItem.ToPlaylistCache): PlaylistItem.Mapper<PlaylistCache>

    @Binds
    @SelectPlaylistScope
    fun providePlaylistDomainToCacheMapper(obj: PlaylistDomainToCacheMapper.Base): PlaylistDomainToCacheMapper

    @Binds
    @SelectPlaylistScope
    fun providePlaylistDomainToPlaylistCacheMapper(obj: PlaylistDomain.ToPlaylistCacheMapper): PlaylistDomain.Mapper<PlaylistCache>

    @Binds
    @SelectPlaylistScope
    fun providePlaylistDomainToContainsMapper(obj: PlaylistDomainToContainsMapper.Base): PlaylistDomainToContainsMapper

    @Binds
    @SelectPlaylistScope
    fun providePlaylistDomainToIdMapper(obj: PlaylistDomainToIdMapper.Base): PlaylistDomainToIdMapper

    @Binds
    @SelectPlaylistScope
    fun provideFavoritesCloudDataSourcePlaylistItem(obj: BaseFavoritesPlaylistsCloudDataSource): FavoritesCloudDataSource<PlaylistItem>

    @Binds
    @SelectPlaylistScope
    fun bindHandleDeletePlaylistRequest(obj: HandleDeletePlaylistRequest): HandleDeleteRequestData<PlaylistCache>

    @Binds
    @SelectPlaylistScope
    fun provideFavoritesCacheDataSourcePlaylists(obj: BaseFavoritesPlaylistsCacheDataSource): FavoritesCacheDataSource<PlaylistCache>

    @Binds
    @SelectPlaylistScope
    fun provideFavoritesPlaylistsRepository(obj: FavoritesPlaylistsRepository.Base): FavoritesPlaylistsRepository

    @Binds
    @SelectPlaylistScope
    fun providePlaylistDomainToIdMapperInt(obj: PlaylistDomainToIdMapperInt.Base): PlaylistDomainToIdMapperInt

    @Binds
    @SelectPlaylistScope
    fun provideFavoritesPlaylistsInteractor(obj: FavoritesPlaylistsInteractor.Base): FavoritesPlaylistsInteractor

    @Binds
    @SelectPlaylistScope
    fun bindPlaylistDataCacheDataSource(obj: PlaylistDataCacheDataSource.Base): PlaylistDataCacheDataSource

    @Binds
    @SelectPlaylistScope
    fun bindAudioIdsToContentIdsMapper(obj: AudioIdsToContentIdsMapper.Base): AudioIdsToContentIdsMapper

    @Binds
    @SelectPlaylistScope
    fun bindPlaylistDataCloudDataSource(obj: PlaylistDataCloudDataSource.Base): PlaylistDataCloudDataSource

    @Binds
    @SelectPlaylistScope
    fun bindAddTrackToPlaylistRepository(obj: AddTrackToPlaylistRepository.Base): AddTrackToPlaylistRepository

    @Binds
    @SelectPlaylistScope
    fun bindAddTrackToPlaylistInteractor(obj: AddTrackToPlaylistInteractor.Base): AddTrackToPlaylistInteractor

    @Binds
    @Reusable
    fun bindFavoritesPlaylistCommunication(obj: FavoritesPlaylistCommunication.Base): FavoritesPlaylistCommunication

    @Binds
    @Reusable
    fun bindFavoritesTracksLoadingCommunication(obj: FavoritesTracksLoadingCommunication.Base): FavoritesTracksLoadingCommunication

    @Binds
    @Reusable
    fun bindPlaylistsUiStateCommunication(obj: PlaylistsUiStateCommunication.Base): PlaylistsUiStateCommunication

    @Binds
    @SelectPlaylistScope
    fun bindFavoritesPlaylistsUiCommuncation(obj: FavoritesPlaylistsUiCommunication.Base): FavoritesPlaylistsUiCommunication


    @Binds
    @SelectPlaylistScope
    fun bindHandlePlaylistsFetchingFromCache(obj: HandlePlaylistsFetchingFromCache.Base): HandlePlaylistsFetchingFromCache

    @Binds
    @SelectPlaylistScope
    fun bindFetchPlaylistsRepository(obj: FetchPlaylistsRepository.Base): FetchPlaylistsRepository

    @Binds
    @SelectPlaylistScope
    fun bindPlaylistsResultUpdateToUiEventMapper(obj: PlaylistsResultUpdateToUiEventMapper.Base): PlaylistsResultUpdateToUiEventMapper

    @Binds
    @SelectPlaylistScope
    fun bindHandleFavoritesPlaylistsFromCache(obj: HandleFavoritesPlaylistsFromCache.Base): HandleFavoritesPlaylistsFromCache

    @Binds
    @SelectPlaylistScope
    fun bindHandleFavoritesPlaylistsUiUpdate(obj: HandleFavoritesPlaylistsUiUpdate.BaseForSelectPlaylist): HandleFavoritesPlaylistsUiUpdate

    @Binds
    @SelectPlaylistScope
    fun bindPlaylistUiToIddMapper(obj: PlaylistUi.ToIdMapper): PlaylistUi.Mapper<String>

    @Binds
    @SelectPlaylistScope
    fun bindCachedPlaylistsCommunication(obj: CachedPlaylistsCommunication.Base): CachedPlaylistsCommunication

    @Binds
    @SelectPlaylistScope
    fun bindSelectPlaylistCommunication(obj: SelectPlaylistCommunication.Base): SelectPlaylistCommunication

    @Binds
    @SelectPlaylistScope
    fun bindSelectPlaylistPlaylistUiStateCommunication(obj: SelectPlaylistUiStateCommunication.Base): SelectPlaylistUiStateCommunication



    @Binds
    @[IntoMap ViewModelKey(SelectPlaylistViewModel::class)]
    fun bindSelectPlaylistViewModel(selectPlaylistViewModel: SelectPlaylistViewModel): ViewModel

}

@Module
class SelectPlaylistModuleProvides{

    @Provides
    @SelectPlaylistScope
    fun providePlaylistDao(db: MusicDatabase): PlaylistDao {
        return db.getPlaylistDao()
    }

    @Provides
    @SelectPlaylistScope
    fun providePlaylistDataService(
        client: OkHttpClient,
        builder: Retrofit.Builder,
        converterFactory: ExtendedGsonConverterFactory
    ): PlaylistDataService {
        return builder
            .baseUrl(baseDataUrl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(PlaylistDataService::class.java)
    }

    @Provides
    @SelectPlaylistScope
    fun providePlaylistsService(
        client: OkHttpClient,
        builder: Retrofit.Builder,
        converterFactory: ExtendedGsonConverterFactory
    ): PlaylistsService {
        return builder
            .baseUrl(baseDataUrl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(PlaylistsService::class.java)
    }

    @Provides
    @SelectPlaylistScope
    fun providePlaylistDomainToContMapper(): PlaylistDomain.Mapper<Pair<String,String>>{
        return PlaylistDomain.ToContainsMapper()
    }

    @Provides
    @SelectPlaylistScope
    fun providePlaylistDomainToIdsMapper(): PlaylistDomain.Mapper<Pair<Int,String>>{
        return  PlaylistDomain.ToIdsMapper()
    }

    @Provides
    @SelectPlaylistScope
    fun providePlaylistDomainToIdMapperrInt(): PlaylistDomain.Mapper<Pair<Int,Int>>{
        return  PlaylistDomain.ToIdsMapperInt()
    }
}