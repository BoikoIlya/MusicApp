package com.kamancho.melisma.frienddetails.di

import androidx.lifecycle.ViewModel
import com.kamancho.melisma.app.core.ExtendedGsonConverterFactory
import com.kamancho.melisma.app.core.MusicDatabase
import com.kamancho.melisma.favorites.presentation.FavoritesStateCommunication
import com.kamancho.melisma.favorites.presentation.FavoritesTrackListCommunication
import com.kamancho.melisma.favorites.presentation.FavoritesTracksCommunication
import com.kamancho.melisma.favorites.presentation.FavoritesTracksLoadingCommunication
import com.kamancho.melisma.favorites.presentation.HandleFavoritesTracksFromCache
import com.kamancho.melisma.main.di.AppModule

import com.kamancho.melisma.main.di.ViewModelKey
import com.kamancho.melisma.frienddetails.data.BaseFriendsDetailsPlaylistsRepository
import com.kamancho.melisma.frienddetails.data.BaseFriendsDetailsTracksRepository
import com.kamancho.melisma.frienddetails.data.FriendsDetailsCacheRepository
import com.kamancho.melisma.frienddetails.data.ModifiedIdToPlaylistCacheMapper
import com.kamancho.melisma.frienddetails.data.ModifiedIdToTrackCacheMapper
import com.kamancho.melisma.frienddetails.data.cache.FriendPlaylistsCacheToUiMapper
import com.kamancho.melisma.frienddetails.data.cache.FriendsAndPlaylistsRelationDao
import com.kamancho.melisma.frienddetails.data.cache.FriendsAndTracksRelationsDao
import com.kamancho.melisma.frienddetails.data.cache.FriendsDetailsCacheDataSource
import com.kamancho.melisma.frienddetails.data.cloud.FriendsDetailsCloudDataSource
import com.kamancho.melisma.frienddetails.domain.FriendDetailsPlaylistsInteractor
import com.kamancho.melisma.frienddetails.domain.FriendDetailsTracksInteractor
import com.kamancho.melisma.frienddetails.presentation.FriendDetailsViewModel
import com.kamancho.melisma.frienddetails.presentation.FriendPlaylistsCommunication
import com.kamancho.melisma.frienddetails.presentation.FriendPlaylistsListCommunication
import com.kamancho.melisma.frienddetails.presentation.FriendPlaylistsLoadingCommunication
import com.kamancho.melisma.frienddetails.presentation.FriendPlaylistsUiStateCommunication
import com.kamancho.melisma.frienddetails.presentation.FriendPlaylistsViewModel
import com.kamancho.melisma.frienddetails.presentation.FriendTracksViewModel
import com.kamancho.melisma.frienddetails.presentation.HandleFriendPlaylistsFromCache
import com.kamancho.melisma.frienddetails.presentation.HandleFriendsPlaylistsUpdate
import com.kamancho.melisma.frienddetails.presentation.HandleFriendsTracksUpdate
import com.kamancho.melisma.userplaylists.data.cache.PlaylistDao
import com.kamancho.melisma.userplaylists.data.cloud.FriendPlaylistsService
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
 * Created by HP on 23.04.2023.
 **/
@Module
interface FriendDetailsModule {

    @Binds
    @FriendDetailsScope
    fun bindPlaylistsUiToDomainMapper(obj: PlaylistUi.ToDomainMapper): PlaylistUi.Mapper<PlaylistDomain>

    @Binds
    @FriendDetailsScope
    fun bindToFriendPlaylistUiWithNormalId(obj: PlaylistUi.ToFriendPlaylistDetailsMapper): PlaylistUi.Mapper<PlaylistUi>

    @Binds
    @FriendDetailsScope
    fun bindModifiedIdToPlaylistCacheMapper(obj: ModifiedIdToPlaylistCacheMapper.Base): ModifiedIdToPlaylistCacheMapper

    @FriendDetailsScope
    @Binds
    fun bindFriendsDetailsCloudDataSource(obj: FriendsDetailsCloudDataSource.Base):
            FriendsDetailsCloudDataSource

    @FriendDetailsScope
    @Binds
    fun bindHandleFriendPlaylistsFromCache(obj: HandleFriendPlaylistsFromCache.Base):
            HandleFriendPlaylistsFromCache

    @FriendDetailsScope
    @Binds
    fun bindFavoritesPlaylistsUiCommunication(obj: FriendPlaylistsCommunication.Base):
            FriendPlaylistsCommunication

    @Reusable
    @Binds
    fun bindPlaylistsUiStateCommunication(obj: FriendPlaylistsUiStateCommunication.Base):
            FriendPlaylistsUiStateCommunication

    @Reusable
    @Binds
    fun bindFavoritesPlaylistCommunication(obj: FriendPlaylistsListCommunication.Base):
            FriendPlaylistsListCommunication

    @Reusable
    @Binds
    fun bindPlaylistLoadingCommunication(obj: FriendPlaylistsLoadingCommunication.Base):
            FriendPlaylistsLoadingCommunication

    @FriendDetailsScope
    @Binds
    fun bindHandleFriendsPlaylistsUpdate(obj: HandleFriendsPlaylistsUpdate.Base):
            HandleFriendsPlaylistsUpdate

    @FriendDetailsScope
    @Binds
    fun bindFriendDetailsPlaylistsInteractor(obj: FriendDetailsPlaylistsInteractor.Base):
            FriendDetailsPlaylistsInteractor

    @FriendDetailsScope
    @Binds
    fun bindFriendPlaylistsCacheToUiMapper(obj: FriendPlaylistsCacheToUiMapper.Base):
            FriendPlaylistsCacheToUiMapper

    @FriendDetailsScope
    @Binds
    fun bindBaseFriendsDetailsPlaylistsRepository(obj: BaseFriendsDetailsPlaylistsRepository.Base):
            BaseFriendsDetailsPlaylistsRepository

    @FriendDetailsScope
    @Binds
    fun bindBaseFriendsDetailsTracksRepository(obj: BaseFriendsDetailsTracksRepository.Base):
            BaseFriendsDetailsTracksRepository


    @Binds
    @FriendDetailsScope
    fun bindFriendsDetailsCacheRepository(obj: FriendsDetailsCacheRepository.Base): FriendsDetailsCacheRepository

    @Binds
    @FriendDetailsScope
    fun bindModifiedIdToTrackCacheMapper(obj: ModifiedIdToTrackCacheMapper.Base): ModifiedIdToTrackCacheMapper

    @Binds
    @FriendDetailsScope
    fun bindFriendDetailsTracksInteractor(obj: FriendDetailsTracksInteractor.Base): FriendDetailsTracksInteractor



    @Reusable
    @Binds
    fun bindFavoritesStateCommunication(obj: FavoritesStateCommunication.Base): FavoritesStateCommunication

    @Reusable
    @Binds
    fun bindFavoritesTrackListCommunication(obj: FavoritesTrackListCommunication.Base): FavoritesTrackListCommunication


    @Reusable
    @Binds
    fun bindFavoritesTracksLoadingCommunication(obj: FavoritesTracksLoadingCommunication.Base): FavoritesTracksLoadingCommunication


    @FriendDetailsScope
    @Binds
    fun bindPlaylistDetailsHandleUiUpdate(obj: HandleFriendsTracksUpdate.Base):
        HandleFriendsTracksUpdate



    @FriendDetailsScope
    @Binds
    fun bindHandleFavoritesTracksFromCache(obj: HandleFavoritesTracksFromCache.Base):
            HandleFavoritesTracksFromCache

    @FriendDetailsScope
    @Binds
    fun bindFriendsDetailsCacheDataSource(obj: FriendsDetailsCacheDataSource.Base):
            FriendsDetailsCacheDataSource

    @Binds
    @[IntoMap ViewModelKey(FriendDetailsViewModel::class)]
    fun bindFriendDetailsViewModel(friendDetailsViewModel: FriendDetailsViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(FriendTracksViewModel::class)]
    fun bindFriendTracksViewModel(friendTracksViewModel: FriendTracksViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(FriendPlaylistsViewModel::class)]
    fun bindFriendPlaylistsViewModel(friendPlaylistsViewModel: FriendPlaylistsViewModel): ViewModel
}

@Module
class FriendDetailsProvidesModule{

    @Provides
    @FriendDetailsScope
    fun provideFriendsAndTracksRelationsDao(db: MusicDatabase): FriendsAndTracksRelationsDao {
        return  db.getFriendsAndTracksRelationDao()
    }

    @Provides
    @FriendDetailsScope
    fun provideFriendsAndPlaylistsRelationDao(db: MusicDatabase): FriendsAndPlaylistsRelationDao {
        return  db.getFriendsAndPlaylistsRelationDao()
    }

    @Provides
    @FriendDetailsScope
    fun providePlaylistDao(db: MusicDatabase): PlaylistDao {
        return  db.getPlaylistDao()
    }

    @Provides
    @FriendDetailsScope
    fun provideFriendPlaylistsService(
        client: OkHttpClient,
        builder: Retrofit.Builder,
        converterFactory: ExtendedGsonConverterFactory,
    ): FriendPlaylistsService {
        return builder
            .baseUrl(AppModule.baseDataUrl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(FriendPlaylistsService::class.java)
    }

    @Provides
    @FriendDetailsScope
    fun provideFavoritesTracksCommunication(
        uiStateCommunication: FavoritesStateCommunication,
        favoritesTrackListCommunication: FavoritesTrackListCommunication,
        favoritesTracksLoadingCommunication: FavoritesTracksLoadingCommunication
    ): FavoritesTracksCommunication{
        return FavoritesTracksCommunication.Base(uiStateCommunication,favoritesTrackListCommunication,favoritesTracksLoadingCommunication)
    }
}