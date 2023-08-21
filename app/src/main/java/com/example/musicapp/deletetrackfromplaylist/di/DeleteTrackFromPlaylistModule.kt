package com.example.musicapp.deletetrackfromplaylist.di

import androidx.lifecycle.ViewModel
import com.example.musicapp.app.core.ExtendedGsonConverterFactory
import com.example.musicapp.creteplaylist.data.PlaylistDataRepository
import com.example.musicapp.creteplaylist.data.cache.PlaylistDataCacheDataSource
import com.example.musicapp.creteplaylist.data.cloud.AudioIdsToContentIdsMapper
import com.example.musicapp.creteplaylist.data.cloud.PlaylistDataCloudDataSource
import com.example.musicapp.creteplaylist.data.cloud.PlaylistDataService
import com.example.musicapp.deletetrackfromplaylist.domain.DeleteTrackFromPlaylistInteractor
import com.example.musicapp.deletetrackfromplaylist.presentation.DeleteTrackFromPlaylistViewModel
import com.example.musicapp.main.di.AppModule

import com.example.musicapp.main.di.ViewModelKey
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