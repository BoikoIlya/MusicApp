package com.example.musicapp.friends.di

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.CacheRepository
import com.example.musicapp.app.core.ExtendedGsonConverterFactory
import com.example.musicapp.app.core.HandleFavoritesTracksSortedSearch
import com.example.musicapp.app.core.MusicDatabase
import com.example.musicapp.favorites.presentation.*
import com.example.musicapp.friends.data.FriendsRepository
import com.example.musicapp.friends.data.cache.FriendsDao
import com.example.musicapp.friends.data.cloud.FriendsCloudDataSource
import com.example.musicapp.friends.data.cloud.FriendsService
import com.example.musicapp.friends.domain.FriendsCacheToFriendsDomainMapper
import com.example.musicapp.friends.domain.FriendsInteractor
import com.example.musicapp.friends.presentation.FriendsCommunication
import com.example.musicapp.friends.presentation.FriendsListCommunication
import com.example.musicapp.friends.presentation.FriendsLoadingCommunication
import com.example.musicapp.friends.presentation.FriendsStateCommunication
import com.example.musicapp.friends.presentation.FriendsViewModel
import com.example.musicapp.friends.presentation.HandleFriendsListFromCache
import com.example.musicapp.friends.presentation.HandleFriendsUpdate
import com.example.musicapp.main.data.cloud.AuthorizationService
import com.example.musicapp.main.di.AppModule
import com.example.musicapp.main.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by HP on 21.03.2023.
 **/
@Module
interface FriendsModule {

    @Binds
    @FriendsScope
    fun bindFriendsInteractor(obj: FriendsInteractor.Base): FriendsInteractor

    @Binds
    @FriendsScope
    fun bindFriendsCloudDataSource(obj: FriendsCloudDataSource.Base): FriendsCloudDataSource

    @Binds
    @FriendsScope
    fun bindFriendsRepository(obj: FriendsRepository.Base): FriendsRepository

    @FriendsScope
    @Binds
    fun bindFriendsCacheToFriendsDomainMapper(obj: FriendsCacheToFriendsDomainMapper.Base): FriendsCacheToFriendsDomainMapper

    @Binds
    @FriendsScope
    fun bindHandleFavoritesTracksFromCache(obj: HandleFriendsUpdate.Base): HandleFriendsUpdate

    @Binds
    @FriendsScope
    fun bindHandleFriendsListFromCache(obj: HandleFriendsListFromCache.Base):HandleFriendsListFromCache


    @Reusable
    @Binds
    fun bindFriendsCommunication(obj: FriendsCommunication.Base): FriendsCommunication

    @Reusable
    @Binds
    fun bindFriendsStateCommunication(obj: FriendsStateCommunication.Base): FriendsStateCommunication

    @Reusable
    @Binds
    fun bindFriendsListCommunication(obj: FriendsListCommunication.Base): FriendsListCommunication

    @Reusable
    @Binds
    fun bindFriendsLoadingCommunication(obj: FriendsLoadingCommunication.Base): FriendsLoadingCommunication

    @Binds
    @[IntoMap ViewModelKey(FriendsViewModel::class)]
    fun bindFriendsViewModel(friendsViewModel: FriendsViewModel): ViewModel
}

@Module
class FriendsProvidesModule{

    @Provides
    @FriendsScope
    fun provideFriendsService(
        client: OkHttpClient,
        builder: Retrofit.Builder,
        converterFactory: ExtendedGsonConverterFactory,
    ): FriendsService {
        return builder
            .baseUrl(AppModule.baseDataUrl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(FriendsService::class.java)
    }

    @Provides
    @FriendsScope
    fun provideFriendsDao(
        db: MusicDatabase
    ): FriendsDao {
        return db.getFriendsDao()
    }
}