package com.kamancho.melisma.artisttracks.di

import androidx.lifecycle.ViewModel
import com.kamancho.melisma.app.core.ExtendedGsonConverterFactory
import com.kamancho.melisma.app.vkdto.TrackItem
import com.kamancho.melisma.artisttracks.data.ArtistTracksRepository
import com.kamancho.melisma.artisttracks.data.cloud.ArtistTracksService
import com.kamancho.melisma.artisttracks.domain.ArtistsTracksInteractor
import com.kamancho.melisma.artisttracks.domain.TracksDomainState
import com.kamancho.melisma.artisttracks.presentation.ArtistTracksCommunication
import com.kamancho.melisma.artisttracks.presentation.ArtistTracksUiStateCommunication
import com.kamancho.melisma.artisttracks.presentation.ArtistsTrackListCommunication
import com.kamancho.melisma.artisttracks.presentation.ArtistTracksListViewModel
import com.kamancho.melisma.artisttracks.presentation.ArtistsTracksViewModel
import com.kamancho.melisma.artisttracks.presentation.PageChangerCommunication
import com.kamancho.melisma.artisttracks.presentation.TracksDomainStateMapper
import com.kamancho.melisma.main.di.AppModule
import com.kamancho.melisma.main.di.ViewModelKey
import com.kamancho.melisma.trending.domain.TrackDomain
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Created by Ilya Boiko @camancho
on 09.12.2023.
 **/
@Module
interface ArtistsTracksModule {


    @Binds
    @ArtistsTracksScope
    fun bindArtistTracksRepository(obj: ArtistTracksRepository.Base): ArtistTracksRepository

    @Binds
    @ArtistsTracksScope
    fun bindArtistsTracksInteractor(obj: ArtistsTracksInteractor.Base): ArtistsTracksInteractor

    @Reusable
    @Binds
    fun bindArtistsTrackListCommunication(obj: ArtistsTrackListCommunication.Base): ArtistsTrackListCommunication

    @Binds
    @Reusable
    fun bindArtistTracksUiStateCommunication(obj: ArtistTracksUiStateCommunication.Base): ArtistTracksUiStateCommunication

    @Binds
    @Reusable
    fun bindArtistTracksCommunication(obj: ArtistTracksCommunication.Base): ArtistTracksCommunication


    @Binds
    @ArtistsTracksScope
    fun bindTracksDomainStateMapper(obj: TracksDomainStateMapper): TracksDomainState.Mapper<Unit>

    @Binds
    @ArtistsTracksScope
    fun bindCloudTrackToTrackDomainMapper(obj: TrackItem.Mapper.CloudTrackToTrackDomainMapper): TrackItem.Mapper<TrackDomain>

    @Binds
    @[IntoMap ViewModelKey(ArtistTracksListViewModel::class)]
    fun bindArtistTracksListViewModel(artistTracksListViewModel: ArtistTracksListViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(ArtistsTracksViewModel::class)]
    fun bindArtistsTracksViewModel(artistsTracksViewModel: ArtistsTracksViewModel): ViewModel

}

@Module
class ArtistsTracksModuleProvides {

    @Provides
    @ArtistsTracksScope
    fun providePlaylistDataService(
     client: OkHttpClient,
     builder: Retrofit.Builder,
     converterFactory: ExtendedGsonConverterFactory,
    ): ArtistTracksService {
        return builder
            .baseUrl(AppModule.baseDataUrl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(ArtistTracksService::class.java)
    }
}