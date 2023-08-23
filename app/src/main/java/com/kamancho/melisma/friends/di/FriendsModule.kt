package com.kamancho.melisma.friends.di

import androidx.lifecycle.ViewModel
import com.kamancho.melisma.app.core.ExtendedGsonConverterFactory
import com.kamancho.melisma.app.core.MusicDatabase
import com.kamancho.melisma.friends.data.FriendsRepository
import com.kamancho.melisma.friends.data.cache.FriendsDao
import com.kamancho.melisma.friends.data.cloud.FriendsCloudDataSource
import com.kamancho.melisma.friends.data.cloud.FriendsService
import com.kamancho.melisma.friends.domain.FriendsCacheToFriendsDomainMapper
import com.kamancho.melisma.friends.domain.FriendsInteractor
import com.kamancho.melisma.friends.presentation.FriendsCommunication
import com.kamancho.melisma.friends.presentation.FriendsListCommunication
import com.kamancho.melisma.friends.presentation.FriendsLoadingCommunication
import com.kamancho.melisma.friends.presentation.FriendsStateCommunication
import com.kamancho.melisma.friends.presentation.FriendsViewModel
import com.kamancho.melisma.friends.presentation.HandleFriendsListFromCache
import com.kamancho.melisma.friends.presentation.HandleFriendsUpdate
import com.kamancho.melisma.main.di.AppModule
import com.kamancho.melisma.main.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import retrofit2.Retrofit

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