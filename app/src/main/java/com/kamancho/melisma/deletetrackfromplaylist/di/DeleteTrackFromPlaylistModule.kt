package com.kamancho.melisma.deletetrackfromplaylist.di

import androidx.lifecycle.ViewModel
import com.kamancho.melisma.app.core.ExtendedGsonConverterFactory
import com.kamancho.melisma.creteplaylist.data.PlaylistDataRepository
import com.kamancho.melisma.creteplaylist.data.cache.PlaylistDataCacheDataSource
import com.kamancho.melisma.creteplaylist.data.cloud.AudioIdsToContentIdsMapper
import com.kamancho.melisma.creteplaylist.data.cloud.PlaylistDataCloudDataSource
import com.kamancho.melisma.creteplaylist.data.cloud.PlaylistDataService
import com.kamancho.melisma.deletetrackfromplaylist.domain.DeleteTrackFromPlaylistInteractor
import com.kamancho.melisma.deletetrackfromplaylist.presentation.DeleteTrackFromPlaylistViewModel
import com.kamancho.melisma.main.di.AppModule

import com.kamancho.melisma.main.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Created by HP on 23.04.2023.
 **/
@Module
interface DeleteTrackFromPlaylistModule {

    @Binds
    @DeleteTrackFromPlaylistScope
    fun bindPlaylistDataCacheDataSource(obj: PlaylistDataCacheDataSource.Base): PlaylistDataCacheDataSource

    @Binds
    @DeleteTrackFromPlaylistScope
    fun bindAudioIdsToContentIdsMapper(obj: AudioIdsToContentIdsMapper.Base): AudioIdsToContentIdsMapper

    @Binds
    @DeleteTrackFromPlaylistScope
    fun bindPlaylistDataCloudDataSource(obj: PlaylistDataCloudDataSource.Base): PlaylistDataCloudDataSource

    @DeleteTrackFromPlaylistScope
    @Binds
    fun bindDeleteTrackFromPlaylistRepository(obj: PlaylistDataRepository.Base):
            PlaylistDataRepository


    @DeleteTrackFromPlaylistScope
    @Binds
    fun bindDeleteTrackFromPlaylistInteractor(obj: DeleteTrackFromPlaylistInteractor.Base):
            DeleteTrackFromPlaylistInteractor


    @Binds
    @[IntoMap ViewModelKey(DeleteTrackFromPlaylistViewModel::class)]
    fun bindDeleteTrackFromPlaylistViewModel(deleteTrackFromPlaylistViewModel: DeleteTrackFromPlaylistViewModel): ViewModel
}

@Module
class DeleteTrackFromPlaylistModuleProvides {

    @Provides
    @DeleteTrackFromPlaylistScope
    fun providePlaylistDataService(
        client: OkHttpClient,
        builder: Retrofit.Builder,
        converterFactory: ExtendedGsonConverterFactory
    ): PlaylistDataService {
        return builder
            .baseUrl(AppModule.baseDataUrl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(PlaylistDataService::class.java)
    }

}