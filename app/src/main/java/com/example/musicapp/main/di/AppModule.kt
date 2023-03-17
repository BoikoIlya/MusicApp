package com.example.musicapp.main.di

import android.content.ComponentName
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.app.main.presentation.*
import com.example.musicapp.app.core.BottomPlayerBarCommunicatin
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.main.data.AuthorizationRepository
import com.example.musicapp.main.data.cache.TokenStore
import com.example.musicapp.main.data.cloud.AuthorizationService
import com.example.musicapp.main.data.cloud.MusicDataService
import com.example.musicapp.main.presentation.BottomPlayerBarState
import com.example.musicapp.player.presentation.PlayerService
import com.example.musicapp.trending.presentation.TrackUi
import com.google.common.util.concurrent.ListenableFuture
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by HP on 29.01.2023.
 **/
@Module
class AppModule {

    companion object{
        private const val baseUrl = "https://api.napster.com"
        private const val baseUrlAuthorization = "https://accounts.spotify.com/"
        private const val baseUrlMusicData = "https://api.spotify.com/v1/"
        private const val shared_pref_name = "settings"
        private const val token_key = "tken_key"
    }

//    @Provides
//    @Singleton
//    fun provideTrendingService(
//        builder: Retrofit.Builder,
//        converterFactory: GsonConverterFactory
//    ): TrendingService {
//        return builder
//            .baseUrl(baseUrl)
//            .addConverterFactory(converterFactory)
//            .build()
//            .create(TrendingService::class.java)
//    }


    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        interceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthorizationService(
        client: OkHttpClient,
        builder: Retrofit.Builder,
        converterFactory: GsonConverterFactory): AuthorizationService {
        return builder
            .baseUrl(baseUrlAuthorization)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(AuthorizationService::class.java)
    }

    @Provides
    @Singleton
    fun provideMusicDataService(
        client: OkHttpClient,
        builder: Retrofit.Builder,
        converterFactory: GsonConverterFactory): MusicDataService {
        return builder
            .baseUrl(baseUrlMusicData)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(MusicDataService::class.java)
    }

    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory{
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
    }

    @Provides
    @Singleton
    fun provideManagerResource(context: Context): ManagerResource {
        return ManagerResource.Base(context)
    }
    @Singleton
    @Provides
    fun provideControllerFuture(context: Context): ListenableFuture<MediaController> {
       return MediaController.Builder(
           context,
           SessionToken(context, ComponentName(context, PlayerService::class.java)))
            .buildAsync()
    }

    @Singleton
    @Provides
    fun provideAuthorizationRepo(service: AuthorizationService, cache: TokenStore): AuthorizationRepository {
        return AuthorizationRepository.Base(service,cache)
    }

    @Singleton
    @Provides
    fun provideSharedPref(context: Context): SharedPreferences{
        return context.getSharedPreferences(shared_pref_name, MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideTokenStore(sharedPreferences: SharedPreferences): TokenStore {
        return TokenStore.Base(token_key, sharedPreferences)
    }
}

@Module
interface AppBindModule{

    @Binds
    @Singleton
    fun bindImageLoaderForPlaylists(obj: ImageLoader.Base): ImageLoader

    @Binds
    @Singleton
    fun bindTrendingTemporaryCache(obj: TemporaryTracksCache.Base): TemporaryTracksCache
    @Singleton
    @Binds
    fun bindPlayerCommunication(obj: PlayerCommunication.Base): PlayerCommunication

    @Singleton
    @Binds
    fun bindSelectedTrackPositionCommunication(obj: SelectedTrackPositionCommunication.Base):
            SelectedTrackPositionCommunication

    @Singleton
    @Binds
    fun bindToPlayStateService(obj: TrackUi.ToPlayStateService):
            TrackUi.Mapper<Unit>
    @Singleton
    @Binds
    fun bindToPlayStateBottomBar(obj: TrackUi.ToPlayStateBottomBar):
            TrackUi.Mapper<BottomPlayerBarState.Play>

    @Singleton
    @Binds
    fun bindPlayerServiceCommunication(obj: PlayerServiceCommunication.Base):
            PlayerServiceCommunication

    @Binds
    @[IntoMap ViewModelKey(MainViewModel::class)]
    fun bindMainActivityViewModel(trendingViewModel: MainViewModel): ViewModel


    @Singleton
    @Binds
    fun bindBottomPlayerBarCommunication(obj: BottomPlayerBarCommunicatin.Base):
            BottomPlayerBarCommunicatin

    @Singleton
    @Binds
    fun bindDispatcherList(obj: DispatchersList.Base): DispatchersList

    @Binds
     fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


}