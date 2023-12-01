package com.kamancho.melisma.userplaylists.di

import androidx.lifecycle.ViewModel
import com.kamancho.melisma.app.core.DeleteInteractor
import com.kamancho.melisma.app.core.ExtendedGsonConverterFactory
import com.kamancho.melisma.app.core.FavoritesCacheDataSource
import com.kamancho.melisma.app.core.FavoritesCloudDataSource
import com.kamancho.melisma.app.core.HandleDeletePlaylistRequest
import com.kamancho.melisma.app.core.HandleDeleteRequestData
import com.kamancho.melisma.app.core.MusicDatabase
import com.kamancho.melisma.app.vkdto.PlaylistItem
import com.kamancho.melisma.favorites.data.cache.PlaylistsCacheToDomainMapper
import com.kamancho.melisma.favorites.presentation.FavoritesTracksLoadingCommunication
import com.kamancho.melisma.favorites.presentation.HandleListFromCache
import com.kamancho.melisma.main.di.AppModule
import com.kamancho.melisma.main.di.ViewModelKey
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
import com.kamancho.melisma.userplaylists.domain.PlaylistsResult
import com.kamancho.melisma.userplaylists.presentation.BaseHandleFavoritesPlaylistsFromCache
import com.kamancho.melisma.userplaylists.presentation.CanEditPlaylistStateCommunication
import com.kamancho.melisma.userplaylists.presentation.FavoritesPlaylistCommunication
import com.kamancho.melisma.userplaylists.presentation.FavoritesPlaylistsUiCommunication
import com.kamancho.melisma.userplaylists.presentation.HandleFavoritesPlaylistsUiUpdate
import com.kamancho.melisma.userplaylists.presentation.HandlePlaylistsFetchingFromCache
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import com.kamancho.melisma.userplaylists.presentation.PlaylistsMenuDialogBottomSheetViewModel
import com.kamancho.melisma.userplaylists.presentation.PlaylistsResultUpdateToUiEventMapper
import com.kamancho.melisma.userplaylists.presentation.PlaylistsUiStateCommunication
import com.kamancho.melisma.userplaylists.presentation.PlaylistsViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Created by HP on 28.01.2023.
 **/


@Module
interface PlaylistsModule{

    @Binds
    @PlaylistsScope
    fun bindPlaylistsCacheToDomainMapper(obj: PlaylistsCacheToDomainMapper.Base): PlaylistsCacheToDomainMapper

    @Binds
    @PlaylistsScope
    fun bindPlaylistUiPlaylistToDomainMapper(obj: PlaylistUi.ToDomainMapper): PlaylistUi.Mapper<PlaylistDomain>

    @Binds
    @PlaylistsScope
    fun bindToPlaylistUiMapper(obj: PlaylistDomain.ToPlaylistUi): PlaylistDomain.Mapper<PlaylistUi>

    @Binds
    @PlaylistsScope
    fun bindPlaylistUiToDomainMapper(obj: PlaylistUiToDomainMapper.Base): PlaylistUiToDomainMapper

    @Binds
    @PlaylistsScope
    fun providePlaylistCloudToCacheMapper(obj: PlaylistCloudToCacheMapper.Base): PlaylistCloudToCacheMapper

    @Binds
    @PlaylistsScope
    fun providePlaylistCloudPlaylistToCacheMapper(obj: PlaylistItem.ToPlaylistCache): PlaylistItem.Mapper<PlaylistCache>

    @Binds
    @PlaylistsScope
    fun providePlaylistDomainToCacheMapper(obj: PlaylistDomainToCacheMapper.Base): PlaylistDomainToCacheMapper

    @Binds
    @PlaylistsScope
    fun providePlaylistDomainToPlaylistCacheMapper(obj: PlaylistDomain.ToPlaylistCacheMapper): PlaylistDomain.Mapper<PlaylistCache>

    @Binds
    @PlaylistsScope
    fun providePlaylistDomainToContainsMapper(obj: PlaylistDomainToContainsMapper.Base): PlaylistDomainToContainsMapper

    @Binds
    @PlaylistsScope
    fun providePlaylistDomainToIdMapper(obj: PlaylistDomainToIdMapper.Base): PlaylistDomainToIdMapper

    @Binds
    @PlaylistsScope
    fun providePlaylistDomainToIdMapperInt(obj: PlaylistDomainToIdMapperInt.Base): PlaylistDomainToIdMapperInt



    @Binds
    @PlaylistsScope
    fun provideFavoritesCloudDataSourcePlaylistItem(obj: BaseFavoritesPlaylistsCloudDataSource): FavoritesCloudDataSource<PlaylistItem>

    @Binds
    @PlaylistsScope
    fun bindHandleDeletePlaylistRequest(obj: HandleDeletePlaylistRequest): HandleDeleteRequestData<PlaylistCache>

    @Binds
    @PlaylistsScope
    fun provideFavoritesCacheDataSourcePlaylists(obj: BaseFavoritesPlaylistsCacheDataSource): FavoritesCacheDataSource<PlaylistCache>

    @Binds
    @PlaylistsScope
    fun provideFavoritesPlaylistsRepository(obj: FavoritesPlaylistsRepository.Base): FavoritesPlaylistsRepository

    @Binds
    @PlaylistsScope
    fun provideFavoritesPlaylistsInteractor(obj: FavoritesPlaylistsInteractor.Base): FavoritesPlaylistsInteractor

    @Binds
    @PlaylistsScope
    fun provideDeleteInteractorPlaylistsResult(obj: FavoritesPlaylistsInteractor.Base): DeleteInteractor<PlaylistsResult>

    @Binds
    @PlaylistsScope
    fun bindHandleFavoritesPlaylistsUiUpdate(obj: HandleFavoritesPlaylistsUiUpdate.BaseForFavoritesPlaylists):HandleFavoritesPlaylistsUiUpdate

    @Binds
    @PlaylistsScope
    fun bindCanEditPlaylistStateCommunication(obj: CanEditPlaylistStateCommunication.Base):CanEditPlaylistStateCommunication

    @Binds
    @PlaylistsScope
    fun bindPlaylistUiCanEdit(obj: PlaylistUi.CanEdit):PlaylistUi.Mapper<Boolean>

    @Binds
    @PlaylistsScope
    fun bindBaseHandleFavoritesPlaylistsFromCache(obj: BaseHandleFavoritesPlaylistsFromCache):HandleListFromCache<PlaylistUi>

    @Binds
    @PlaylistsScope
    fun bindPlaylistsResultUpdateToUiEventMapper(obj: PlaylistsResultUpdateToUiEventMapper.Base): PlaylistsResultUpdateToUiEventMapper


    @Binds
    @PlaylistsScope
    fun bindFavoritesPlaylistCommunication(obj: FavoritesPlaylistCommunication.Base): FavoritesPlaylistCommunication

    @Binds
    @PlaylistsScope
    fun bindPlaylistsUiStateCommunication(obj: PlaylistsUiStateCommunication.Base):PlaylistsUiStateCommunication

    @Binds
    @Reusable
    fun bindFavoritesTracksLoadingCommunication(obj: FavoritesTracksLoadingCommunication.Base):FavoritesTracksLoadingCommunication

    @Binds
    @PlaylistsScope
    fun bindFavoritesPlaylistsUiCommuncation(obj: FavoritesPlaylistsUiCommunication.Base):FavoritesPlaylistsUiCommunication

    @Binds
    @PlaylistsScope
    fun bindPlaylistUiToIdMapper(obj: PlaylistUi.ToIdMapper): PlaylistUi.Mapper<String>

    @Binds
    @PlaylistsScope
    fun bindHandlePlaylistsFetchingFromCache(obj: HandlePlaylistsFetchingFromCache.Base): HandlePlaylistsFetchingFromCache

    @Binds
    @PlaylistsScope
    fun bindFetchPlaylistsRepository(obj: FetchPlaylistsRepository.Base): FetchPlaylistsRepository


    @Binds
    @[IntoMap ViewModelKey(PlaylistsViewModel::class)]
    fun bindPlaylistsViewModel(playlistsViewModel: PlaylistsViewModel): ViewModel


    @Binds
    @[IntoMap ViewModelKey(PlaylistsMenuDialogBottomSheetViewModel::class)]
    fun bindPlaylistsMenuDialogBottomSheetViewModel(playlistsMenuDialogBottomSheetViewModel: PlaylistsMenuDialogBottomSheetViewModel): ViewModel

}

@Module
class PlaylistsModuleProvides{

    @Provides
    @PlaylistsScope
    fun providePlaylistsService(
        client: OkHttpClient,
        builder: Retrofit.Builder,
        converterFactory: ExtendedGsonConverterFactory
    ): PlaylistsService {
        return builder
            .baseUrl(AppModule.baseDataUrl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(PlaylistsService::class.java)
    }

    @Provides
    @PlaylistsScope
    fun providePlaylistDao(db: MusicDatabase): PlaylistDao {
        return db.getPlaylistDao()
    }

    @Provides
    @PlaylistsScope
    fun providePlaylistDomainToContMapper(): PlaylistDomain.Mapper<Pair<String,String>>{
        return PlaylistDomain.ToContainsMapper()
    }

    @Provides
    @PlaylistsScope
    fun providePlaylistDomainToIdsMapper(): PlaylistDomain.Mapper<Pair<Int,String>>{
        return  PlaylistDomain.ToIdsMapper()
    }

    @Provides
    @PlaylistsScope
    fun providePlaylistDomainToIdMapperrInt(): PlaylistDomain.Mapper<Pair<Int,Int>>{
        return  PlaylistDomain.ToIdsMapperInt()
    }
}