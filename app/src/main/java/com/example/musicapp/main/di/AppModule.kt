package com.example.musicapp.main.di


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
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.musicapp.addtoplaylist.domain.SelectedTrackDomain
import com.example.musicapp.addtoplaylist.domain.SelectedTracksInteractor
import com.example.musicapp.addtoplaylist.presentation.SelectedTrackUi
import com.example.musicapp.app.core.*
import com.example.musicapp.app.vkdto.PlaylistItem
import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.creteplaylist.data.PlaylistDataRepository
import com.example.musicapp.creteplaylist.data.cache.PlaylistDataCacheDataSource
import com.example.musicapp.creteplaylist.data.cloud.AudioIdsToContentIdsMapper
import com.example.musicapp.creteplaylist.data.cloud.PlaylistDataCloudDataSource
import com.example.musicapp.creteplaylist.data.cloud.PlaylistDataService
import com.example.musicapp.creteplaylist.presentation.PlaylistDataUiStateCommunication
import com.example.musicapp.creteplaylist.presentation.SelectedTracksStore
import com.example.musicapp.editplaylist.data.PlaylistTracksRepository
import com.example.musicapp.editplaylist.data.cache.PlaylistTracksCacheDataSource
import com.example.musicapp.editplaylist.data.cloud.PlaylistTracksCloudDataSource
import com.example.musicapp.editplaylist.data.cloud.PlaylistTracksService
import com.example.musicapp.editplaylist.domain.PlaylistTracksInteractor
import com.example.musicapp.favorites.data.cache.BaseFavoritesTracksCacheDataSource
import com.example.musicapp.favorites.data.FavoritesTracksRepository
import com.example.musicapp.favorites.data.cache.DomainToContainsMapper
import com.example.musicapp.favorites.data.cache.DomainToDataIdsMapper
import com.example.musicapp.favorites.data.cache.PlaylistsAndTracksDao
import com.example.musicapp.favorites.data.cache.PlaylistsCacheToDomainMapper
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.favorites.data.cache.TrackDomainToCacheMapper
import com.example.musicapp.favorites.data.cache.TracksDao
import com.example.musicapp.favorites.data.cloud.BaseFavoritesTracksCloudDataSource
import com.example.musicapp.favorites.data.cloud.FavoritesService
import com.example.musicapp.favorites.data.cloud.TracksCloudToCacheMapper
import com.example.musicapp.favorites.domain.FavoritesTracksInteractor
import com.example.musicapp.favorites.presentation.ResetSwipeActionCommunication
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.data.AuthorizationRepository
import com.example.musicapp.main.data.CheckAuthRepository
import com.example.musicapp.main.data.cache.AccountDataStore
import com.example.musicapp.main.data.cache.OwnerIdStore
import com.example.musicapp.main.data.cache.TokenStore
import com.example.musicapp.main.data.cloud.AuthorizationService
import com.example.musicapp.trending.data.cloud.TrendingService
import com.example.musicapp.main.presentation.*
import com.example.musicapp.musicdialog.presentation.AddTrackDialogViewModel
import com.example.musicapp.player.presentation.PlayerService
import com.example.musicapp.player.presentation.PlayingTrackIdCommunication
import com.example.musicapp.player.presentation.ShuffleModeEnabledCommunication
import com.example.musicapp.player.presentation.TrackPlaybackPositionCommunication
import com.example.musicapp.playlist.data.cache.PlaylistIdTransfer
import com.example.musicapp.playlist.data.cloud.PlaylistService
import com.example.musicapp.search.data.cloud.SearchTrackService
import com.example.musicapp.searchhistory.data.cache.HistoryDao
import com.example.musicapp.searchhistory.data.cache.SearchQueryTransfer
import com.example.musicapp.selectplaylist.data.AddTrackToPlaylistRepository
import com.example.musicapp.selectplaylist.domain.AddTrackToPlaylistInteractor
import com.example.musicapp.trending.domain.TrackDomain
import com.example.musicapp.trending.presentation.MediaControllerWrapper
import com.example.musicapp.updatesystem.data.MainViewModelMapper
import com.example.musicapp.updatesystem.data.UpdateSystemRepository
import com.example.musicapp.updatesystem.data.cloud.UpdateFirebaseService
import com.example.musicapp.updatesystem.presentation.UpdateDialogViewModel
import com.example.musicapp.userplaylists.data.FavoritesPlaylistsRepository
import com.example.musicapp.userplaylists.data.cache.BaseFavoritesPlaylistsCacheDataSource
import com.example.musicapp.userplaylists.data.cache.PlaylistCache
import com.example.musicapp.userplaylists.data.cache.PlaylistDao
import com.example.musicapp.userplaylists.data.cloud.BaseFavoritesPlaylistsCloudDataSource
import com.example.musicapp.userplaylists.data.cloud.PlaylistCloudToCacheMapper
import com.example.musicapp.userplaylists.data.cloud.PlaylistsService
import com.example.musicapp.userplaylists.domain.FavoritesPlaylistsInteractor
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import com.example.musicapp.userplaylists.domain.PlaylistDomainToCacheMapper
import com.example.musicapp.userplaylists.domain.PlaylistDomainToContainsMapper
import com.example.musicapp.userplaylists.domain.PlaylistDomainToIdMapper
import com.example.musicapp.userplaylists.domain.PlaylistUiToDomainMapper
import com.example.musicapp.userplaylists.domain.PlaylistsResult
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import com.google.common.util.concurrent.ListenableFuture
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Binds
import dagger.Module
import dagger.Provides
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

        private const val baseUrlAuthorization = "https://oauth.vk.com/"
        private const val baseUrlMusicData = "https://api.vk.com/"
        private const val data_store_name = "settings"
        private const val token_key = "tken_key"
        private const val owner_id_key = "owner_id_key"
        private const val db_name = "music_app_db"
        private const val topic_name = "update_topic"
        private const val test_topic_name = "test_topic_name"
        const val api_version = "5.131"
        const val mainPlaylistId = 0 //playlist of current account music
    }

    @Provides
    @Singleton
    fun provideMainPlaylist(): ContentValues{
        val contentValues = ContentValues()
        contentValues.put("playlistId", mainPlaylistId)
        contentValues.put("title", "")
        contentValues.put("is_following", true)
        contentValues.put("count", 0)
        contentValues.put("create_time", 0)
        contentValues.put("description", "")
        contentValues.put("owner_id", 0)
        contentValues.put("thumbs","")
        return contentValues
    }

//    @Provides
//    @Singleton
//    fun provideMigrationDBFrom1To2(): Migration {
//        return object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("CREATE TABLE IF NOT EXISTS `history_table` " +
//                        "(`queryTerm` TEXT NOT NULL, `time` INTEGER NOT NULL, PRIMARY KEY(`queryTerm`))")
//            }
//        }
//    }

    @Provides
    @Singleton
    fun provideMusicAppDB(
        context: Context,
       // migration: Migration,
        mainPlaylist: ContentValues
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
    fun providePlaylistDao(db: MusicDatabase): PlaylistDao{
        return db.getPlaylistDao()
    }

    @Provides
    @Singleton
    fun providePlaylistsAndTracksDao(db: MusicDatabase): PlaylistsAndTracksDao{
        return db.getPlaylistsAndTracksDao()
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
                HttpLoggingInterceptor.Level.BODY
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
        converterFactory: ExtendedGsonConverterFactory): AuthorizationService {
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
        converterFactory: ExtendedGsonConverterFactory): TrendingService {
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
        converterFactory: ExtendedGsonConverterFactory): SearchTrackService {
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
        converterFactory: ExtendedGsonConverterFactory
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
    fun provideFavoritesService(
        client: OkHttpClient,
        builder: Retrofit.Builder,
        converterFactory: ExtendedGsonConverterFactory
    ): FavoritesService {
        return builder
            .baseUrl(baseUrlMusicData)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(FavoritesService::class.java)
    }

    @Provides
    @Singleton
    fun providePlaylistsService(
        client: OkHttpClient,
        builder: Retrofit.Builder,
        converterFactory: ExtendedGsonConverterFactory
    ): PlaylistsService {
        return builder
            .baseUrl(baseUrlMusicData)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(PlaylistsService::class.java)
    }

    @Provides
    @Singleton
    fun providePlaylistDataService(
        client: OkHttpClient,
        builder: Retrofit.Builder,
        converterFactory: ExtendedGsonConverterFactory
    ): PlaylistDataService  {
        return builder
            .baseUrl(baseUrlMusicData)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(PlaylistDataService::class.java)
    }

    @Provides
    @Singleton
    fun providePlaylistTracksService(
        client: OkHttpClient,
        builder: Retrofit.Builder,
        converterFactory: ExtendedGsonConverterFactory
    ): PlaylistTracksService  {
        return builder
            .baseUrl(baseUrlMusicData)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(PlaylistTracksService::class.java)
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
        db: MusicDatabase
    ): AuthorizationRepository {
        return AuthorizationRepository.Base(service,cache,handleError,db)
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

    @Provides
    @Singleton
    fun  provideImageLoaderForPlaylists(context: Context): ImageLoader {
        return ImageLoader.Base(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) PaintBackgroundShadow.ApiPAndAbove(context)
            else PaintBackgroundShadow.BelowApiP
        )
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
    fun provideContainsTrackMapper(obj: TrackDomain.ContainsTrackMapper):  TrackDomain.Mapper<Pair<String,String>>{
        return TrackDomain.ContainsTrackMapper()
    }

    @Provides
    @Singleton
    fun providePlaylistDomainToContMapper(): PlaylistDomain.Mapper<Pair<String,String>>{
        return PlaylistDomain.ToContainsMapper()
    }

    @Provides
    @Singleton
    fun providePlaylistDomainToIdsMapper(): PlaylistDomain.Mapper<Pair<Int,Int>>{
        return PlaylistDomain.ToIdsMapper()
    }

    @Provides
    @Singleton
    fun provideBlurEffectAnimator(): BlurEffectAnimator{
       return if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.S) BlurEffectAnimator.Api31AndAbove()
        else BlurEffectAnimator.BelowApi31()
    }
}

@Module
interface AppBindModule{

//    @Binds
//    @Singleton
//    fun bindBaseHandleFavoritesPlaylistsFromCache(obj: BaseHandleFavoritesPlaylistsFromCache): HandleFavoritesListFromCache<PlaylistUi>

//    @Binds
//    @Singleton
//    fun bindPlaylistUiToIdMapper(obj: PlaylistUi.ToIdMapper):PlaylistUi.Mapper<Int>

//    @Binds
//    @Singleton
//    fun bindPlaylistsResultUpdateToUiEventMapper(obj: PlaylistsResultUpdateToUiEventMapper.Base): PlaylistsResultUpdateToUiEventMapper

    @Binds
    @Singleton
    fun bindPlaylistTracksCloudDataSource(obj: PlaylistTracksCloudDataSource.Base): PlaylistTracksCloudDataSource

    @Binds
    @Singleton
    fun bindSelectedTrackDomainToUi(obj: SelectedTrackDomain.ToIdMapper): SelectedTrackDomain.Mapper<Int>

    @Binds
    @Singleton
    fun bindSelectedTrackUiToDomain(obj: SelectedTrackUi.ToDomain): SelectedTrackUi.Mapper<SelectedTrackDomain>

    @Binds
    @Singleton
    fun bindTracksCacheToSelectedTracksDomainMapper(obj: TracksCacheToSelectedTracksDomainMapper.Base): TracksCacheToSelectedTracksDomainMapper

    @Binds
    @Singleton
    fun bindSelectedTracksInteractor(obj: SelectedTracksInteractor.Base): SelectedTracksInteractor

    @Binds
    @Singleton
    fun bindAddTrackToPlaylistRepository(obj: AddTrackToPlaylistRepository.Base): AddTrackToPlaylistRepository

    @Binds
    @Singleton
    fun bindAddTrackToPlaylistInteractor(obj: AddTrackToPlaylistInteractor.Base): AddTrackToPlaylistInteractor

    @Binds
    @Singleton
    fun bindPlaylistTracksCacheDataSource(obj: PlaylistTracksCacheDataSource.Base): PlaylistTracksCacheDataSource

    @Binds
    @Singleton
    fun bindPlaylistTracksRepository(obj: PlaylistTracksRepository.Base): PlaylistTracksRepository

    @Binds
    @Singleton
    fun bindPlaylistTracksInteractor(obj: PlaylistTracksInteractor.Base): PlaylistTracksInteractor

    @Binds
    @Singleton
    fun bindPlaylistDataRepository(obj: PlaylistDataRepository.Base): PlaylistDataRepository

    @Binds
    @Singleton
    fun bindAudioIdsToContentIdsMapper(obj: AudioIdsToContentIdsMapper.Base): AudioIdsToContentIdsMapper

    @Binds
    @Singleton
    fun bindPlaylistDataCacheDataSource(obj: PlaylistDataCacheDataSource.Base): PlaylistDataCacheDataSource

    @Binds
    @Singleton
    fun bindPlaylistDataCloudDataSource(obj: PlaylistDataCloudDataSource.Base): PlaylistDataCloudDataSource


    @Binds
    @Singleton
    fun bindPlaylistDataUiStateCommunication(obj: PlaylistDataUiStateCommunication.Base): PlaylistDataUiStateCommunication

    @Binds
    @Singleton
    fun bindSelectedTracksStore(obj: SelectedTracksStore.Base): SelectedTracksStore

    @Binds
    @Singleton
    fun bindCachedTracksRepositoryBaseMediaItem(obj: CachedTracksRepository.BaseMediaItem): CachedTracksRepository<MediaItem>

    @Binds
    @Singleton
    fun bindCachedTracksRepositoryBaseSelected(obj: CachedTracksRepository.BaseSelected): CachedTracksRepository<SelectedTrackDomain>










    @Binds
    @Singleton
    fun bindHandleDeletePlaylistRequest(obj: HandleDeletePlaylistRequest): HandleDeleteRequestData<PlaylistCache>



    @Binds
    @Singleton
    fun bindPlaylistUiPlaylistToDomainMapper(obj: PlaylistUi.ToDomainMapper): PlaylistUi.Mapper<PlaylistDomain>

    @Binds
    @Singleton
    fun bindPlaylistsCacheToDomainMapper(obj: PlaylistsCacheToDomainMapper.Base):PlaylistsCacheToDomainMapper

    @Binds
    @Singleton
    fun bindToPlaylistUiMapper(obj: PlaylistDomain.ToPlaylistUi): PlaylistDomain.Mapper<PlaylistUi>

    @Binds
    @Singleton
    fun bindPlaylistUiToDomainMapper(obj: PlaylistUiToDomainMapper.Base): PlaylistUiToDomainMapper

    @Binds
    @Singleton
    fun bindDataTransferPlaylistDomain(obj: DataTransfer.PlaylistTransfer): DataTransfer<PlaylistDomain>

    @Binds
    @Singleton
    fun provideFavoritesCacheDataSourcePlaylists(obj: BaseFavoritesPlaylistsCacheDataSource): FavoritesCacheDataSource<PlaylistCache>

    @Binds
    @Singleton
    fun provideFavoritesCloudDataSourcePlaylistItem(obj: BaseFavoritesPlaylistsCloudDataSource): FavoritesCloudDataSource<PlaylistItem>

    @Binds
    @Singleton
    fun providePlaylistCloudToCacheMapper(obj: PlaylistCloudToCacheMapper.Base): PlaylistCloudToCacheMapper

    @Binds
    @Singleton
    fun providePlaylistCloudPlaylistToCacheMapper(obj: PlaylistItem.ToPlaylistCache): PlaylistItem.Mapper<PlaylistCache>

    @Binds
    @Singleton
    fun provideFavoritesPlaylistsRepository(obj: FavoritesPlaylistsRepository.Base): FavoritesPlaylistsRepository

    @Binds
    @Singleton
    fun provideFavoritesPlaylistsInteractor(obj: FavoritesPlaylistsInteractor.Base): FavoritesPlaylistsInteractor

    @Binds
    @Singleton
    fun provideDeleteInteractorPlaylistsResult(obj: FavoritesPlaylistsInteractor.Base): DeleteInteractor<PlaylistsResult>

    @Binds
    @Singleton
    fun providePlaylistDomainToCacheMapper(obj: PlaylistDomainToCacheMapper.Base): PlaylistDomainToCacheMapper

    @Binds
    @Singleton
    fun providePlaylistDomainToPlaylistCacheMapper(obj: PlaylistDomain.ToPlaylistCacheMapper): PlaylistDomain.Mapper<PlaylistCache>

    @Binds
    @Singleton
    fun providePlaylistDomainToContainsMapper(obj: PlaylistDomainToContainsMapper.Base): PlaylistDomainToContainsMapper

    @Binds
    @Singleton
    fun providePlaylistDomainToIdMapper(obj: PlaylistDomainToIdMapper.Base): PlaylistDomainToIdMapper














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
    fun bindToMediaItemMapper(obj: ToMediaItemMapper): Mapper<TrackCache, MediaItem>

    @Binds
    @Singleton
    fun bindMainViewModelMapper(obj: MainViewModelMapper.Base): MainViewModelMapper


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
    fun bindShuffleModeEnabledCommunication(obj: ShuffleModeEnabledCommunication.Base): ShuffleModeEnabledCommunication

    @Singleton
    @Binds
    fun bindDeleteDialogActionCommunication(obj: ResetSwipeActionCommunication.Base): ResetSwipeActionCommunication


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
    @[IntoMap ViewModelKey(AddTrackDialogViewModel::class)]
    fun bindMusicDialogViewModel(musicDialogViewModel: AddTrackDialogViewModel): ViewModel

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