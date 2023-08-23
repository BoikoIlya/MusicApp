package com.example.musicapp.main.di


import android.app.DownloadManager
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.CacheKeyFactory
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.musicapp.app.core.*
import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.captcha.data.CaptchaCommunication
import com.example.musicapp.captcha.data.CaptchaRepository
import com.example.musicapp.captcha.data.cache.CaptchaDataStore
import com.example.musicapp.captcha.presentation.CaptchaDialogViewModel
import com.example.musicapp.captcha.presentation.CaptchaErrorCommunication
import com.example.musicapp.captcha.presentation.DismissDialogCommunication
import com.example.musicapp.creteplaylist.presentation.SelectedTracksCommunication
import com.example.musicapp.creteplaylist.presentation.SelectedTracksStore
import com.example.musicapp.downloader.data.DownloadTracksRepository
import com.example.musicapp.downloader.data.cache.DownloadFolderPathStore
import com.example.musicapp.downloader.data.cache.DownloadTracksCacheDataSource
import com.example.musicapp.downloader.data.cloud.DownloadCloudDataSource
import com.example.musicapp.downloader.domain.DownloadInteractor
import com.example.musicapp.favorites.data.FavoritesTracksRepository
import com.example.musicapp.favorites.data.cache.BaseFavoritesTracksCacheDataSource
import com.example.musicapp.favorites.data.cache.DomainToContainsMapper
import com.example.musicapp.favorites.data.cache.DomainToDataIdsMapper
import com.example.musicapp.favorites.data.cache.PlaylistsAndTracksDao
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.favorites.data.cache.TrackDomainToCacheMapper
import com.example.musicapp.favorites.data.cache.TracksDao
import com.example.musicapp.favorites.data.cloud.BaseFavoritesTracksCloudDataSource
import com.example.musicapp.favorites.data.cloud.FavoritesService
import com.example.musicapp.favorites.data.cloud.TracksCloudToCacheMapper
import com.example.musicapp.favorites.domain.FavoritesTracksInteractor
import com.example.musicapp.favorites.presentation.ResetSwipeActionCommunication
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.downloader.presentation.DownloadCompleteCommunication
import com.example.musicapp.main.data.AuthorizationRepository
import com.example.musicapp.main.data.CheckAuthRepository
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.data.cache.AccountDataStore
import com.example.musicapp.main.data.cache.OwnerIdStore
import com.example.musicapp.main.data.cache.TokenStore
import com.example.musicapp.main.data.cloud.AuthorizationService
import com.example.musicapp.main.presentation.*
import com.example.musicapp.musicdialog.presentation.AddTrackDialogViewModel
import com.example.musicapp.player.presentation.PlayerService
import com.example.musicapp.player.presentation.PlayingTrackIdCommunication
import com.example.musicapp.player.presentation.TrackPlaybackPositionCommunication
import com.example.musicapp.favoritesplaylistdetails.data.cache.PlaylistIdTransfer
import com.example.musicapp.frienddetails.domain.FriendIdAndNameTransfer
import com.example.musicapp.frienddetails.presentation.SearchQueryFriendCommunication
import com.example.musicapp.notifications.data.NotificationIdsRepository
import com.example.musicapp.notifications.data.NotificationsRepository
import com.example.musicapp.notifications.data.cache.NotificationsIdsDataStore
import com.example.musicapp.notifications.data.cloud.NotificationsFirebaseService
import com.example.musicapp.notifications.domain.CheckForPermissions
import com.example.musicapp.notifications.domain.NotificationsInteractor
import com.example.musicapp.notifications.presentation.NotificationIconBadgeCommunication
import com.example.musicapp.notifications.presentation.NotificationListCommunication
import com.example.musicapp.notifications.presentation.NotificationsResultToUiBadgeMapper
import com.example.musicapp.notifications.presentation.NotificationsResultToUiMapper
import com.example.musicapp.notifications.presentation.NotificationsUiStateCommunication
import com.example.musicapp.notifications.presentation.NotificationsUpdateOnAppStart
import com.example.musicapp.notifications.presentation.NotificationsViewModel
import com.example.musicapp.searchhistory.data.cache.SearchQueryTransfer
import com.example.musicapp.searchhistory.presentation.SearchQueryCommunication
import com.example.musicapp.settings.presentation.DownloadedTracksSizeCommunication
import com.example.musicapp.settings.presentation.DownloadsPathCommunication
import com.example.musicapp.settings.presentation.SettingsViewModel
import com.example.musicapp.trending.domain.TrackDomain
import com.example.musicapp.trending.presentation.MediaControllerWrapper
import com.example.musicapp.userplaylists.data.cache.PlaylistDao
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import com.google.common.util.concurrent.ListenableFuture
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.multibindings.IntoMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File
import javax.inject.Singleton

/**
 * Created by HP on 29.01.2023.
 **/
@Module
class AppModule {

    companion object{
        private const val baseUrlAuthorization = "https://oauth.vk.com/"
        const val baseDataUrl = "https://api.vk.com/"
        private const val data_store_name = "settings"
        private const val token_key = "tken_key"
        private const val owner_id_key = "owner_id_key"
        private const val notifications_ids_key = "notifications_ids_key"
        private const val downloads_file_path_key = "downloads_file_path_key"
        private const val db_name = "music_app_db"
        private const val topic_name = "update_topic"
        private const val test_topic_name = "test_topic_name"
        const val api_version = "5.91"//"5.131"
        const val mainPlaylistId = Int.MIN_VALUE //playlist of current account music
    }

    @Provides
    @Singleton
    fun provideMainPlaylist(): ContentValues{
        val contentValues = ContentValues()
        contentValues.put("playlistId", mainPlaylistId.toString())
        contentValues.put("title", "")
        contentValues.put("is_following", true)
        contentValues.put("count", 0)
        contentValues.put("update_time", 0)
        contentValues.put("description", "")
        contentValues.put("owner_id", 0)
        contentValues.put("thumbs","")
        return contentValues
    }



    @Provides
    @Singleton
    fun provideMusicAppDB(
        context: Context,
        mainPlaylist: ContentValues,
    ): MusicDatabase{
        return Room.databaseBuilder(
            context,
            MusicDatabase::class.java,
            db_name
        ).addCallback(object : RoomDatabase.Callback(){
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    db.insert(
                        PlaylistDao.playlists_table,
                        OnConflictStrategy.IGNORE,
                        mainPlaylist
                    )
                }
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideTracksDao(db: MusicDatabase): TracksDao{
        return db.getTracksDao()
    }



    @Provides
    @Singleton
    fun providePlaylistsAndTracksDao(db: MusicDatabase): PlaylistsAndTracksDao{
        return db.getPlaylistsAndTracksDao()
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
        interceptor: Interceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideFavoritesService(
        client: OkHttpClient,
        builder: Retrofit.Builder,
        converterFactory: ExtendedGsonConverterFactory,
    ): FavoritesService {
        return builder
            .baseUrl(baseDataUrl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(FavoritesService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthorizationService(
        client: OkHttpClient,
        builder: Retrofit.Builder,
        converterFactory: ExtendedGsonConverterFactory,
    ): AuthorizationService {
        return builder
            .baseUrl(baseUrlAuthorization)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(AuthorizationService::class.java)
    }


    @Provides
    @Singleton
    fun provideConverterFactory(): ExtendedGsonConverterFactory{
        return ExtendedGsonConverterFactory.create()
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
    fun provideAuthorizationRepo(
        service: AuthorizationService,
        cache: AccountDataStore,
        handleError: HandleError,
        db: MusicDBManager,
        imageLoader: ImageLoader,
    ): AuthorizationRepository {
        return AuthorizationRepository.Base(service,cache,handleError,db,imageLoader)
    }

    @Singleton
    @Provides
    fun provideSettingsDataStore(context: Context, dispatchersList: DispatchersList): DataStore<Preferences>{
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(context,data_store_name)),
            scope = CoroutineScope(dispatchersList.io() + SupervisorJob()),
            produceFile ={ context.preferencesDataStoreFile(data_store_name) }
        )
    }

    @Singleton
    @Provides
    fun provideTokenStore(dataStore: DataStore<Preferences>): TokenStore {
        return TokenStore.Base(stringPreferencesKey(token_key), dataStore)
    }

    @Singleton
    @Provides
    fun provideOwnerIdStore(dataStore: DataStore<Preferences>): OwnerIdStore {
        return OwnerIdStore.Base(stringPreferencesKey(owner_id_key), dataStore)
    }

    @Singleton
    @Provides
    fun provideNotificationsIdsDataStore(dataStore: DataStore<Preferences>): NotificationsIdsDataStore{
        return NotificationsIdsDataStore.Base(stringSetPreferencesKey(notifications_ids_key),dataStore)
    }


    @Provides
    @Singleton
    fun  provideImageLoaderForPlaylists(context: Context): ImageLoader {
        return ImageLoader.Base(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) PaintBackgroundShadow.ApiPAndAbove
            else PaintBackgroundShadow.BelowApiP,
            context
        )
    }

    @Provides
    @Singleton
    fun provideFirebaseMessagingWrapper(): FirebaseMessagingWrapper{
        return FirebaseMessagingWrapper.Base(FirebaseMessaging.getInstance(), topic_name)
    }

    @Provides
    @Singleton
    fun provideAddToFavoritesCloudMapper(): TrackDomain.Mapper<Pair<Int, Int>>{
        return TrackDomain.AddToFavoritesCloudMapper()
    }

    @Provides
    @Singleton
    fun provideMediaItemToTrackDomain(): MediaItemToTrackDomainMapper{
        return MediaItemToTrackDomainMapper.Base()
    }

    @Provides
    @Singleton
    fun provideContainsTrackMapper():  TrackDomain.Mapper<Pair<String,String>>{
        return TrackDomain.ContainsTrackMapper()
    }




    @Provides
    @Singleton
    fun provideBlurEffectAnimator(): BlurEffectAnimator{
       return  BlurEffectAnimator.Base()
    }



    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideDownloadFolderPathStore(dataStore: DataStore<Preferences>): DownloadFolderPathStore {
        return DownloadFolderPathStore.Base(stringPreferencesKey(downloads_file_path_key),dataStore)
    }

    @Provides
    @Singleton
    fun provideDownloadManager(context: Context): DownloadManager {
        return context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }
}

@Module
interface AppBindModule{

    @Singleton
    @Binds
    fun bindSearchQueryFriendCommunication(obj: SearchQueryFriendCommunication.Base):
            SearchQueryFriendCommunication

    @Singleton
    @Binds
    fun bindDownloadsPathCommunication(obj: DownloadsPathCommunication.Base):
            DownloadsPathCommunication

    @Singleton
    @Binds
    fun bindDownloadedTracksSizeCommunication(obj: DownloadedTracksSizeCommunication.Base):
            DownloadedTracksSizeCommunication

    @Singleton
    @Binds
    fun bindDownloadInteractor(obj: DownloadInteractor.Base):
            DownloadInteractor

    @Singleton
    @Binds
    fun bindDownloadTracksRepository(obj: DownloadTracksRepository.Base):
            DownloadTracksRepository

    @Singleton
    @Binds
    fun bindDownloadCloudDataSource(obj: DownloadCloudDataSource.Base):
            DownloadCloudDataSource

    @Singleton
    @Binds
    fun bindDownloadTracksCacheDataSource(obj: DownloadTracksCacheDataSource.Base):
            DownloadTracksCacheDataSource

    @Singleton
    @Binds
    fun bindNotificationIconBadgeCommunication(obj: NotificationIconBadgeCommunication.Base):
            NotificationIconBadgeCommunication

    @Singleton
    @Binds
    fun bindNotificationsResultToUiBadgeMapper(obj: NotificationsResultToUiBadgeMapper.Base):
            NotificationsResultToUiBadgeMapper

    @Singleton
    @Binds
    fun bindNotificationIdsRepository(obj: NotificationIdsRepository.Base):
            NotificationIdsRepository



    @Singleton
    @Binds
    fun bindNotificationsUpdateOnAppStart(obj: NotificationsUpdateOnAppStart.Base):
            NotificationsUpdateOnAppStart

    @Singleton
    @Binds
    fun bindNotificationListCommunication(obj: NotificationListCommunication.Base):
            NotificationListCommunication

    @Singleton
    @Binds
    fun bindNotificationsUiStateCommunication(obj: NotificationsUiStateCommunication.Base):
            NotificationsUiStateCommunication

    @Singleton
    @Binds
    fun bindNotificationsResultToUiMapper(obj: NotificationsResultToUiMapper.Base):
            NotificationsResultToUiMapper

    @Singleton
    @Binds
    fun bindSearchNotificationsInteractor(obj: NotificationsInteractor.Base):
            NotificationsInteractor

    @Singleton
    @Binds
    fun bindSearchCheckForPermissions(obj: CheckForPermissions.Base):
            CheckForPermissions

    @Singleton
    @Binds
    fun bindSearchNotificationsRepository(obj: NotificationsRepository.Base):
            NotificationsRepository

    @Singleton
    @Binds
    fun bindNotificationsFirebaseService(obj: NotificationsFirebaseService.Base):
            NotificationsFirebaseService

    @Singleton
    @Binds
    fun providesSearchQueryCommunication(obj: SearchQueryCommunication.Base): SearchQueryCommunication

    @Singleton
    @Binds
    fun providesFriendIdAndNameTransfer(obj: FriendIdAndNameTransfer.Base): FriendIdAndNameTransfer


    @Binds
    @Singleton
    fun bindMediaIdTransfer(obj: DataTransfer.MediaIdTransfer): DataTransfer<String>


    @Binds
    @Singleton
    fun bindHLSCachingCompleteCommunication(obj: DownloadCompleteCommunication.Base): DownloadCompleteCommunication

    @Binds
    @Singleton
    fun bindMusicDBManager(obj: MusicDBManager.Base): MusicDBManager

    @Binds
    @Singleton
    fun bindCaptchaErrorCommunication(obj: CaptchaErrorCommunication.Base): CaptchaErrorCommunication

    @Binds
    @Singleton
    fun bindDismissDialogCommunication(obj: DismissDialogCommunication.Base): DismissDialogCommunication

    @Binds
    @Singleton
    fun bindCaptchaRepository(obj: CaptchaRepository.Base): CaptchaRepository

    @Binds
    @Singleton
    fun bindCaptchaDataStore(obj: CaptchaDataStore.Base): CaptchaDataStore

    @Binds
    @Singleton
    fun bindCaptchaCommunication(obj: CaptchaCommunication.Base): CaptchaCommunication

    @Binds
    @Singleton
    fun bindDataTransferPlaylistDomain(obj: DataTransfer.PlaylistTransfer): DataTransfer<PlaylistDomain>

    @Binds
    @Singleton
    fun bindTrackDomainToCacheMapper(obj: TrackDomainToCacheMapper.Base): TrackDomainToCacheMapper

    @Binds
    @Singleton
    fun bindSDKChecker(obj: SDKChecker.Base): SDKChecker

    @Binds
    @Singleton
    fun bindPermissionCheckCommunication(obj: PermissionCheckCommunication.Base): PermissionCheckCommunication

    @Binds
    @Singleton
    fun bindHandleDeleteTrackRequest(obj: HandleDeleteTrackRequest): HandleDeleteRequestData<TrackCache>

    @Binds
    @Singleton
    fun bindTracksCloudToCacheMapper(obj: TracksCloudToCacheMapper.Base): TracksCloudToCacheMapper

    @Binds
    @Singleton
    fun bindDomainToContainsMapper(obj: DomainToContainsMapper.Base): DomainToContainsMapper

    @Binds
    @Singleton
    fun bindDomainToDataIdsMapper(obj: DomainToDataIdsMapper.Base): DomainToDataIdsMapper

    @Binds
    @Singleton
    fun bindFavoritesTracksCloudDataSource(obj: BaseFavoritesTracksCloudDataSource): FavoritesCloudDataSource<TrackItem>

    @Binds
    @Singleton
    fun bindFavoritesTracksCacheDataSource(obj: BaseFavoritesTracksCacheDataSource): FavoritesCacheDataSource<TrackCache>

    @Binds
    @Singleton
    fun bindTrackUiMapper(obj: TrackDomain.ToTrackUiMapper): TrackDomain.Mapper<MediaItem>

    @Binds
    @Singleton
    fun bindPlayingTrackIdCommunication(obj: PlayingTrackIdCommunication.Base): PlayingTrackIdCommunication

    @Singleton
    @Binds
    fun bindHandlePlayerError(obj: HandlePlayerError.Base): HandlePlayerError

    @Singleton
    @Binds
    fun bindTrackDomainToTrackCacheMapper(obj: TrackDomain.ToTrackCacheMapper): TrackDomain.Mapper<TrackCache>

    @Singleton
    @Binds
    fun bindDeleteTrackMapper(obj: TrackDomain.DeleteTrackMapper): TrackDomain.Mapper<Int>

    @Singleton
    @Binds
    fun bindFormatTimeSecondsToMinutesAndSeconds(obj: FormatTimeSecondsToMinutesAndSeconds.Base):FormatTimeSecondsToMinutesAndSeconds

    @Singleton
    @Binds
    fun bindConnectionChecker(obj: ConnectionChecker.Base): ConnectionChecker

    @Singleton
    @Binds
    fun bindCheckAuthRepository(obj: AuthorizationRepository.Base): CheckAuthRepository

    @Singleton
    @Binds
    fun bindFavoritesTracksInteractor(obj: FavoritesTracksInteractor.Base): FavoritesTracksInteractor

    @Singleton
    @Binds
    fun bindTrackChecker(obj: TrackChecker.Base): TrackChecker

    @Binds
    @Singleton
    fun bindHandleUnauthorizedResponseTrendingResult(obj: HandleResponse.Base): HandleResponse

    @Binds
    @Singleton
    fun bindMusicDialogAndNewIdTransfer(obj: DataTransfer.MusicDialogTransfer): DataTransfer<TrackDomain>

    @Binds
    @Singleton
    fun bindUpdateDialogTransfer(obj: DataTransfer.UpdateDialogTransfer.Base): DataTransfer.UpdateDialogTransfer

    @Binds
    @Singleton
    fun bindPlaylistIdTransfer(obj: PlaylistIdTransfer.Base): PlaylistIdTransfer

    @Binds
    @Singleton
    fun bindToMediaItemMapper(obj: ToMediaItemMapper.Base):  ToMediaItemMapper


    @Singleton
    @Binds
    fun bindCloudTrackToTrackCacheMapper(obj: TrackItem.Mapper.CloudTrackToTrackCacheMapper): TrackItem.Mapper<TrackCache>

    @Binds
    @Singleton
    fun bindFavoritesRepo(obj: FavoritesTracksRepository.Base): FavoritesTracksRepository

    @Binds
    @Singleton
    fun bindInteractor(obj: FavoritesTracksInteractor.Base): Interactor<MediaItem,TracksResult>

    @Binds
    @Singleton
    fun bindMediaControllerWrapper(mediaControllerWrapper: MediaControllerWrapper.Base): MediaControllerWrapper

    @Binds
    @Singleton
    fun bindTrendingTemporaryCache(obj: TemporaryTracksCache.Base): TemporaryTracksCache

    @Singleton
    @Binds
    fun bindPlayerCommunication(obj: PlayerCommunication.Base): PlayerCommunication




    @Singleton
    @Binds
    fun bindSingleUiEventCommunication(obj: GlobalSingleUiEventCommunication.Base):
            GlobalSingleUiEventCommunication

    @Singleton
    @Binds
    fun bindSelectedTrackPositionCommunication(obj: SelectedTrackCommunication.Base):
            SelectedTrackCommunication


    @Singleton
    @Binds
    fun bindCurrentQueueCommunication(obj: CurrentQueueCommunication.Base):
            CurrentQueueCommunication

    @Singleton
    @Binds
    fun bindAccountDataStore(obj: AccountDataStore.Base):
            AccountDataStore

    @Singleton
    @Binds
    fun bindFragmentManagerCommunication(obj: ActivityNavigationCommunication.Base):
            ActivityNavigationCommunication

    @Singleton
    @Binds
    fun bindSearchQueryAndKeyBoardFocusTransfer(obj: SearchQueryTransfer.Base):
            SearchQueryTransfer


    @Singleton
    @Binds
    fun bindTrackDurationCommunication(obj: TrackDurationCommunication.Base):
            TrackDurationCommunication



    @Singleton
    @Binds
    fun bindSlideViewPagerCommunication(obj: SlideViewPagerCommunication.Base):
            SlideViewPagerCommunication

    @Singleton
    @Binds
    fun bindBottomPlayerBarCommunication(obj: PlayerControlsCommunication.Base):
            PlayerControlsCommunication

    @Reusable
    @Binds
    fun bindTrackPositionCommunication(communication: TrackPlaybackPositionCommunication.Base): TrackPlaybackPositionCommunication

    @Singleton
    @Binds
    fun bindBottomSheetCommunication(obj: BottomSheetCommunication.Base):
            BottomSheetCommunication

    @Singleton
    @Binds
    fun bindDispatcherList(obj: DispatchersList.Base): DispatchersList


    @Binds
    @Singleton
    fun bindTracksResultToSingleUiEventCommunicationMapper(obj: TracksResultToUiEventCommunicationMapper.Base): TracksResultToUiEventCommunicationMapper

    @Binds
    @Singleton
    fun bindDeleteDialogActionCommunication(obj: ResetSwipeActionCommunication.Base): ResetSwipeActionCommunication

    @Binds
    @Singleton
    fun bindHandleError(obj: HandleError.Base): HandleError

    @Binds
    @Singleton
    fun provideSelectedTracksCommunication(obj: SelectedTracksCommunication.Base): SelectedTracksCommunication

    @Binds
    @Singleton
    fun bindSelectedTracksStore(obj: SelectedTracksStore.Base): SelectedTracksStore

    @Binds
    @[IntoMap ViewModelKey(CaptchaDialogViewModel::class)]
    fun bindCaptchaDialogViewModel(captchaDialogViewModel: CaptchaDialogViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(MainViewModel::class)]
    fun bindMainActivityViewModel(trendingViewModel: MainViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(AddTrackDialogViewModel::class)]
    fun bindMusicDialogViewModel(musicDialogViewModel: AddTrackDialogViewModel): ViewModel


    @Binds
    @[IntoMap ViewModelKey(NotificationsViewModel::class)]
    fun bindNotificationsViewModel(NotificationsViewModel: NotificationsViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(SettingsViewModel::class)]
    fun bindSettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}