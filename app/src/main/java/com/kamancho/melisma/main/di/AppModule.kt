package com.kamancho.melisma.main.di


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
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.kamancho.melisma.app.core.*
import com.kamancho.melisma.app.vkdto.TrackItem
import com.kamancho.melisma.captcha.data.CaptchaCommunication
import com.kamancho.melisma.captcha.data.CaptchaRepository
import com.kamancho.melisma.captcha.data.cache.CaptchaDataStore
import com.kamancho.melisma.captcha.presentation.CaptchaDialogViewModel
import com.kamancho.melisma.captcha.presentation.CaptchaErrorCommunication
import com.kamancho.melisma.captcha.presentation.DismissDialogCommunication
import com.kamancho.melisma.creteplaylist.presentation.SelectedTracksCommunication
import com.kamancho.melisma.creteplaylist.presentation.SelectedTracksStore
import com.kamancho.melisma.downloader.data.DownloadTracksRepository
import com.kamancho.melisma.downloader.data.cache.DownloadFolderPathStore
import com.kamancho.melisma.downloader.data.cache.DownloadTracksCacheDataSource
import com.kamancho.melisma.downloader.data.cloud.DownloadCloudDataSource
import com.kamancho.melisma.downloader.domain.DownloadInteractor
import com.kamancho.melisma.favorites.data.FavoritesTracksRepository
import com.kamancho.melisma.favorites.data.cache.BaseFavoritesTracksCacheDataSource
import com.kamancho.melisma.favorites.data.cache.DomainToContainsMapper
import com.kamancho.melisma.favorites.data.cache.DomainToDataIdsMapper
import com.kamancho.melisma.favorites.data.cache.PlaylistsAndTracksDao
import com.kamancho.melisma.favorites.data.cache.TrackCache
import com.kamancho.melisma.favorites.data.cache.TrackDomainToCacheMapper
import com.kamancho.melisma.favorites.data.cache.TracksDao
import com.kamancho.melisma.favorites.data.cloud.BaseFavoritesTracksCloudDataSource
import com.kamancho.melisma.favorites.data.cloud.FavoritesService
import com.kamancho.melisma.favorites.data.cloud.TracksCloudToCacheMapper
import com.kamancho.melisma.favorites.domain.FavoritesTracksInteractor
import com.kamancho.melisma.favorites.presentation.ResetSwipeActionCommunication
import com.kamancho.melisma.favorites.presentation.TracksResult
import com.kamancho.melisma.downloader.presentation.DownloadCompleteCommunication
import com.kamancho.melisma.main.data.AuthorizationRepository
import com.kamancho.melisma.main.data.CheckAuthRepository
import com.kamancho.melisma.main.data.TemporaryTracksCache
import com.kamancho.melisma.main.data.cache.AccountDataStore
import com.kamancho.melisma.main.data.cache.OwnerIdStore
import com.kamancho.melisma.main.data.cache.TokenStore
import com.kamancho.melisma.main.data.cloud.AuthorizationService
import com.kamancho.melisma.main.presentation.*
import com.kamancho.melisma.musicdialog.presentation.AddTrackDialogViewModel
import com.kamancho.melisma.player.presentation.PlayerService
import com.kamancho.melisma.player.presentation.PlayingTrackIdCommunication
import com.kamancho.melisma.player.presentation.TrackPlaybackPositionCommunication
import com.kamancho.melisma.frienddetails.presentation.SearchQueryFriendCommunication
import com.kamancho.melisma.notifications.data.NotificationIdsRepository
import com.kamancho.melisma.notifications.data.NotificationsRepository
import com.kamancho.melisma.notifications.data.cache.NotificationsIdsDataStore
import com.kamancho.melisma.notifications.data.cloud.NotificationsFirebaseService
import com.kamancho.melisma.notifications.domain.CheckForPermissions
import com.kamancho.melisma.notifications.domain.NotificationsInteractor
import com.kamancho.melisma.notifications.presentation.NotificationIconBadgeCommunication
import com.kamancho.melisma.notifications.presentation.NotificationListCommunication
import com.kamancho.melisma.notifications.presentation.NotificationsResultToUiBadgeMapper
import com.kamancho.melisma.notifications.presentation.NotificationsResultToUiMapper
import com.kamancho.melisma.notifications.presentation.NotificationsUiStateCommunication
import com.kamancho.melisma.notifications.presentation.NotificationsUpdateOnAppStart
import com.kamancho.melisma.notifications.presentation.NotificationsViewModel
import com.kamancho.melisma.searchhistory.data.cache.SearchQueryTransfer
import com.kamancho.melisma.searchhistory.presentation.SearchQueryCommunication
import com.kamancho.melisma.settings.presentation.DownloadedTracksSizeCommunication
import com.kamancho.melisma.settings.presentation.DownloadsPathCommunication
import com.kamancho.melisma.settings.presentation.SettingsViewModel
import com.kamancho.melisma.trending.domain.TrackDomain
import com.kamancho.melisma.trending.presentation.MediaControllerWrapper
import com.kamancho.melisma.userplaylists.data.cache.PlaylistDao
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import com.google.common.util.concurrent.ListenableFuture
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.kamancho.melisma.BuildConfig
import com.kamancho.melisma.artisttracks.data.cache.ArtistsTracksCacheDataSource
import com.kamancho.melisma.artisttracks.di.ArtistsTracksScope
import com.kamancho.melisma.artisttracks.presentation.PageChangerCommunication
import com.kamancho.melisma.main.data.cloud.AuthorizationCloudDataSource
import com.kamancho.melisma.update.UpdateManager
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
import javax.inject.Singleton

/**
 * Created by HP on 29.01.2023.
 **/
@Module
class AppModule {

    companion object{
        @androidx.annotation.Keep
        private const val baseUrlAuthorization = "https://oauth.vk.com/"
        @androidx.annotation.Keep
        const val baseDataUrl = "https://api.vk.com/"
        private const val data_store_name = "settings"
        private const val token_key = "tken_key"
        private const val owner_id_key = "owner_id_key"
        private const val notifications_ids_key = "notifications_ids_key"
        private const val downloads_file_path_key = "downloads_file_path_key"
        private const val db_name = "melisma_app_db"
        @androidx.annotation.Keep
        private const val topic_name = "update_topic"
        private const val test_topic_name = "test_topic_name"
        @androidx.annotation.Keep
        const val api_version = "5.91"//"5.131"
        @androidx.annotation.Keep
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
        migration: Migration
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
            .addMigrations(migration)
            .build()
    }

    @Provides
    @Singleton
    fun provideRoomMigration1_to_2(): Migration{
        return object : Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ${TracksDao.table_name} ADD COLUMN artistsIds TEXT DEFAULT '' NOT NULL")
            }

        }
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
                if(BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BODY
                    //HttpLoggingInterceptor.Level.BASIC
                else HttpLoggingInterceptor.Level.NONE
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
        cloud: AuthorizationCloudDataSource,
        cache: AccountDataStore,
        db: MusicDBManager,
        imageLoader: ImageLoader,
    ): AuthorizationRepository {
        return AuthorizationRepository.Base(cloud,cache,db,imageLoader)
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
    fun provideFirebaseInitializer(): FirebaseInitializer{
        return FirebaseInitializer.Base(
            if(BuildConfig.DEBUG) test_topic_name
            else topic_name
            )
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
    fun provideNotificationsFirebaseService(
        connectionChecker: ConnectionChecker
    ): NotificationsFirebaseService{
        return NotificationsFirebaseService.Base(connectionChecker = connectionChecker)
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

    @Provides
    @Singleton
    fun provideUpdateManager(context: Context): UpdateManager {
        return UpdateManager.Base(AppUpdateManagerFactory.create(context))
    }
}

@Module
interface AppBindModule{


    @Binds
    @Singleton
    fun bindPageChangerCommunication(obj: PageChangerCommunication.Base): PageChangerCommunication

    @Singleton
    @Binds
    fun bindArtistsTracksCacheDataSource(obj: ArtistsTracksCacheDataSource.Base): ArtistsTracksCacheDataSource

    @Singleton
    @Binds
    fun bindAuthorizationCloudDataSource(obj: AuthorizationCloudDataSource.Base): AuthorizationCloudDataSource

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
    fun providesSearchQueryCommunication(obj: SearchQueryCommunication.Base): SearchQueryCommunication




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