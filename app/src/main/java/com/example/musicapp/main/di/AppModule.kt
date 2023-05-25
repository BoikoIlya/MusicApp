package com.example.musicapp.main.di

import android.content.ComponentName
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.BuildConfig
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.musicapp.app.core.*
import com.example.musicapp.favorites.data.FavoriteTracksRepository
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.favorites.data.cache.TracksDao
import com.example.musicapp.favorites.presentation.FavoritesTrackListCommunication
import com.example.musicapp.favorites.presentation.FavoritesStateCommunication
import com.example.musicapp.favorites.presentation.TracksCommunication
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.data.AuthorizationRepository
import com.example.musicapp.main.data.cache.TokenStore
import com.example.musicapp.main.data.cloud.AuthorizationService
import com.example.musicapp.trending.data.cloud.TrendingService
import com.example.musicapp.main.presentation.*
import com.example.musicapp.musicdialog.presentation.MusicDialogViewModel
import com.example.musicapp.player.presentation.IsSavedCommunication
import com.example.musicapp.player.presentation.PlayerService
import com.example.musicapp.player.presentation.ShuffleModeEnabledCommunication
import com.example.musicapp.player.presentation.TrackPlaybackPositionCommunication
import com.example.musicapp.playlist.data.cache.PlaylistIdTransfer
import com.example.musicapp.playlist.data.cloud.PlaylistService
import com.example.musicapp.search.data.cloud.SearchTrackService
import com.example.musicapp.searchhistory.data.cache.HistoryDao
import com.example.musicapp.searchhistory.data.cache.SearchQueryTransfer
import com.example.musicapp.trending.presentation.MediaControllerWrapper
import com.example.musicapp.trending.presentation.TrackUi
import com.example.musicapp.trending.presentation.TrendingResult
import com.example.musicapp.updatesystem.data.MainViewModelMapper
import com.example.musicapp.updatesystem.data.UpdateSystemRepository
import com.example.musicapp.updatesystem.data.cloud.UpdateFirebaseService
import com.example.musicapp.updatesystem.presentation.UpdateDialogViewModel
import com.google.common.util.concurrent.ListenableFuture
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
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

@UnstableApi /**
 * Created by HP on 29.01.2023.
 **/
@Module
class AppModule {

    companion object{
        private const val baseUrlAuthorization = "https://accounts.spotify.com/"
        private const val baseUrlMusicData = "https://api.spotify.com/v1/"
        private const val shared_pref_name = "settings"
        private const val token_key = "tken_key"
        private const val db_name = "music_app_db"
        private const val topic_name = "update_topic"
        private const val test_topic_name = "test_topic_name"

    }

    @Provides
    @Singleton
    fun provideMigrationDBFrom1To2(): Migration {
        return  object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `history_table` " +
                        "(`queryTerm` TEXT NOT NULL, `time` INTEGER NOT NULL, PRIMARY KEY(`queryTerm`))")
            }
        }
    }

    @Provides
    @Singleton
    fun provideMusicAppDB(context: Context, migration: Migration): MusicDatabase{
        return Room.databaseBuilder(
            context,
            MusicDatabase::class.java,
            db_name
        ).addMigrations(migration).build()
    }

    @Provides
    @Singleton
    fun provideTracksDao(db: MusicDatabase): TracksDao{
        return db.getTracksDao()
    }

    @Provides
    @Singleton
    fun provideHistoryDao(db: MusicDatabase): HistoryDao{
        return db.getHistoryDao()
    }

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return HttpLoggingInterceptor()
            .setLevel(
                HttpLoggingInterceptor.Level.NONE
            )
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
        converterFactory: GsonConverterFactory): TrendingService {
        return builder
            .baseUrl(baseUrlMusicData)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(TrendingService::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchTracksService(
        client: OkHttpClient,
        builder: Retrofit.Builder,
        converterFactory: GsonConverterFactory): SearchTrackService {
        return builder
            .baseUrl(baseUrlMusicData)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(SearchTrackService::class.java)
    }

    @Provides
    @Singleton
    fun providePlaylistService(
        client: OkHttpClient,
        builder: Retrofit.Builder,
        converterFactory: GsonConverterFactory
    ): PlaylistService {
        return builder
            .baseUrl(baseUrlMusicData)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(PlaylistService::class.java)
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


    @Singleton
    @Provides
    fun provideUpdateFirebaseService(): UpdateFirebaseService.Base{
        return UpdateFirebaseService.Base(Firebase.firestore)
    }

    @Provides
    @Singleton
    fun provideFirebaseMessagingWrapper(): FirebaseMessagingWrapper{
        return FirebaseMessagingWrapper.Base(FirebaseMessaging.getInstance(), topic_name)
    }

}

@Module
interface AppBindModule{

    @Binds
    @Singleton
    fun bindHandleUnauthorizedResponseTrendingResult(obj: HandleResponse.Base<TrendingResult>): HandleResponse<TrendingResult>
    @Binds
    @Singleton
    fun bindMediaItemTransfer(obj: DataTransfer.MusicDialogTransfer): DataTransfer<MediaItem>
    @Binds
    @Singleton
    fun bindUpdateDialogTransfer(obj: DataTransfer.UpdateDialogTransfer.Base): DataTransfer.UpdateDialogTransfer
    @Binds
    @Singleton
    fun bindPlaylistIdTransfer(obj: PlaylistIdTransfer.Base): PlaylistIdTransfer

    @Binds
    @Singleton
    fun bindToMediaItemMapper(obj: ToMediaItemMapper): Mapper<TrackCache, MediaItem>

    @Binds
    @Singleton
    fun bindMainViewModelMapper(obj: MainViewModelMapper.Base): MainViewModelMapper

    @Binds
    @Singleton
    fun bindToTrackCacheMapper(obj: ToTrackCacheMapper): Mapper<MediaItem, TrackCache>
    @Binds
    @Singleton
    fun bindFavoritesRepo(obj: FavoriteTracksRepository.Base): FavoriteTracksRepository

    @Binds
    @Singleton
    fun bindTracksRepo(obj: FavoriteTracksRepository.Base): TracksRepository

    @Binds
    @Singleton
    fun bindMediaControllerWrapper(mediaControllerWrapper: MediaControllerWrapper.Base): MediaControllerWrapper

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
    fun bindShuffleModeEnabledCommunication(obj: ShuffleModeEnabledCommunication.Base): ShuffleModeEnabledCommunication

    @Singleton
    @Binds
    fun bindSingleUiEventCommunication(obj: SingleUiEventCommunication.Base):
            SingleUiEventCommunication

    @Singleton
    @Binds
    fun bindSelectedTrackPositionCommunication(obj: SelectedTrackCommunication.Base):
            SelectedTrackCommunication

    @Singleton
    @Binds
    fun bindToPlayStateService(obj: TrackUi.ToPlayStateService):
            TrackUi.Mapper<Unit>
    @Singleton
    @Binds
    fun bindToPlayStateBottomBar(obj: TrackUi.ToPlayStateBottomBar):
            TrackUi.Mapper<PlayerControlsState.Play>

    @Singleton
    @Binds
    fun bindCurrentQueueCommunication(obj: CurrentQueueCommunication.Base):
            CurrentQueueCommunication

    @Singleton
    @Binds
    fun bindSearchQueryAndKeyBoardFocusTransfer(obj: SearchQueryTransfer.Base):
            SearchQueryTransfer

    @Singleton
    @Binds
    fun bindSlideViewPagerCommunication(obj: SlideViewPagerCommunication.Base):
            SlideViewPagerCommunication
    @Singleton
    @Binds
    fun bindTrackDurationCommunication(obj: TrackDurationCommunication.Base):
            TrackDurationCommunication

    @Binds
    @[IntoMap ViewModelKey(MainViewModel::class)]
    fun bindMainActivityViewModel(trendingViewModel: MainViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(MusicDialogViewModel::class)]
    fun bindMusicDialogViewModel(musicDialogViewModel: MusicDialogViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(UpdateDialogViewModel::class)]
    fun bindUpdateDialogViewModel(updateDialogViewModel: UpdateDialogViewModel): ViewModel


    @Singleton
    @Binds
    fun bindBottomPlayerBarCommunication(obj: PlayerControlsCommunication.Base):
            PlayerControlsCommunication

    @Singleton
    @Binds
    fun bindBottomSheetCommunication(obj: BottomSheetCommunication.Base):
            BottomSheetCommunication

    @Singleton
    @Binds
    fun bindDispatcherList(obj: DispatchersList.Base): DispatchersList

    @Binds
     fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory



    @Binds
    @Singleton
    fun bindIsSavedCommunication(communication: IsSavedCommunication.Base): IsSavedCommunication

    @Binds
    @Singleton
    fun bindTrackPositionCommunication(communication: TrackPlaybackPositionCommunication.Base): TrackPlaybackPositionCommunication

    @Binds
    @Singleton
    fun bingUpdateSystemRepository(obj: UpdateSystemRepository.Base): UpdateSystemRepository

    @Binds
    @Singleton
    fun bindTracksResultToSingleUiEventCommunicationMapper(obj: TracksResultToUiEventCommunicationMapper.Base): TracksResultToUiEventCommunicationMapper


    @Binds
    @Singleton
    fun bindHandleError(obj: HandleError.Base): HandleError




}