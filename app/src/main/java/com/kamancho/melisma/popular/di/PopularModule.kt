package com.kamancho.melisma.popular.di

import androidx.lifecycle.ViewModel
import com.kamancho.melisma.app.core.ExtendedGsonConverterFactory
import com.kamancho.melisma.main.di.AppModule
import com.kamancho.melisma.main.di.ViewModelKey
import com.kamancho.melisma.popular.data.PopularRepository
import com.kamancho.melisma.popular.data.PopularsService
import com.kamancho.melisma.popular.domain.PopularInteractor
import com.kamancho.melisma.popular.presentation.PopularCommunication
import com.kamancho.melisma.popular.presentation.PopularListCommunication
import com.kamancho.melisma.popular.presentation.PopularUiStateCommunication
import com.kamancho.melisma.popular.presentation.PopularViewModel
import com.kamancho.melisma.popular.presentation.TracksResultToPopularTracksCommunicationMapper
import com.kamancho.melisma.trending.domain.TrackDomain
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Created by HP on 01.05.2023.
 **/
@Module
interface PopularModule {


    @Binds
    @PopularScope
    fun bindTracksResultToPopularTracksCommunicationMapper(obj: TracksResultToPopularTracksCommunicationMapper.Base): TracksResultToPopularTracksCommunicationMapper

    @Binds
    @PopularScope
    fun bindPopularCommunication(obj: PopularCommunication.Base): PopularCommunication

    @Binds
    @Reusable
    fun bindPopularListCommunication(obj: PopularListCommunication.Base): PopularListCommunication

    @Binds
    @Reusable
    fun bindPopularUiStateCommunication(obj: PopularUiStateCommunication.Base): PopularUiStateCommunication

    @Binds
    @PopularScope
    fun bindPopularInteractor(obj: PopularInteractor.Base): PopularInteractor

    @Binds
    @PopularScope
    fun bindPopularRepository(obj: PopularRepository.Base): PopularRepository

    @Binds
    @PopularScope
    fun bindCloudTrackToTrackDomainMapper(obj: com.kamancho.melisma.app.vkdto.TrackItem.Mapper.CloudTrackToTrackDomainMapper): com.kamancho.melisma.app.vkdto.TrackItem.Mapper<TrackDomain>

    @Binds
    @[IntoMap ViewModelKey(PopularViewModel::class)]
    fun bindPopularViewModel(popularViewModel: PopularViewModel): ViewModel



}


@Module
class PopularModuleProvides{


    @Provides
    @PopularScope
    fun providePopularsService(
        client: OkHttpClient,
        builder: Retrofit.Builder,
        converterFactory: ExtendedGsonConverterFactory
    ): PopularsService {
        return builder
            .baseUrl(AppModule.baseDataUrl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(PopularsService::class.java)
    }


}