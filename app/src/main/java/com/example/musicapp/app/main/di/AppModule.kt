package com.example.musicapp.app.main.di

import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.musicapp.app.main.data.TemporaryTracksCache
import com.example.musicapp.app.main.presentation.*
import com.example.musicapp.app.core.BottomPlayerBarCommunicatin
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.main.data.cloud.AuthorizationService
import com.example.musicapp.player.presentation.PlayerService
import com.example.musicapp.trending.data.cloud.TrendingService
import com.example.musicapp.trending.presentation.TrackUi
import com.google.common.util.concurrent.ListenableFuture
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
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
    }

    @Provides
    @Singleton
    fun provideTrendingService(
        builder: Retrofit.Builder,
        converterFactory: GsonConverterFactory
    ): TrendingService {
        return builder
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .build()
            .create(TrendingService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthorizationService(
        builder: Retrofit.Builder,
        converterFactory: GsonConverterFactory): AuthorizationService {
        return builder
            .baseUrl(baseUrlAuthorization)
            .addConverterFactory(converterFactory)
            .build()
            .create(AuthorizationService::class.java)
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

}

@Module
interface AppBindModule{

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
    @[IntoMap ViewModelKey(MainActivityViewModel::class)]
    fun bindMainActivityViewModel(trendingViewModel: MainActivityViewModel): ViewModel


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