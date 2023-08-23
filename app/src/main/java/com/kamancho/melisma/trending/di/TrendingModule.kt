package com.kamancho.melisma.trending.di

import androidx.lifecycle.ViewModel
import com.kamancho.melisma.app.core.ExtendedGsonConverterFactory
import com.kamancho.melisma.main.di.AppModule
import com.kamancho.melisma.main.di.ViewModelKey
import com.kamancho.melisma.trending.data.TrendingRepository
import com.kamancho.melisma.trending.data.cloud.TrendingService
import com.kamancho.melisma.trending.domain.TopBarItemDomain
import com.kamancho.melisma.trending.domain.TrackDomain
import com.kamancho.melisma.trending.domain.TrendingInteractor
import com.kamancho.melisma.trending.presentation.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Created by HP on 28.01.2023.
 **/


@Module
interface TrendingModule{



    @Binds
    @TrendingScope
    fun bindCloudTrackToTrackDomainMapper(obj: com.kamancho.melisma.app.vkdto.TrackItem.Mapper.CloudTrackToTrackDomainMapper): com.kamancho.melisma.app.vkdto.TrackItem.Mapper<TrackDomain>

    @Binds
    @TrendingScope
    fun bindRepository(obj: TrendingRepository.Base): TrendingRepository



    @Binds
    @TrendingScope
    fun bindTrendingInteractor(obj: TrendingInteractor.Base): TrendingInteractor

    @Binds
    @TrendingScope
    fun bindHandleTrendingResult(obj: HandleTrendingResult.Base): HandleTrendingResult

    @Binds
    @TrendingScope
    fun bindTrendingStateCommunication(obj: TrendingStateCommunication.Base): TrendingStateCommunication

    @Binds
    @TrendingScope
    fun bindTrendingPlaylistCommunication(obj: TrendingPlaylistsCommunication.Base): TrendingPlaylistsCommunication

    @Binds
    @TrendingScope
    fun bindTrendingTracksCommunication(obj: TrendingTracksCommunication.Base): TrendingTracksCommunication

    @Binds
    @TrendingScope
    fun bindTrendingCommunication(obj: TrendingCommunication.Base): TrendingCommunication

    @Binds
    @TrendingScope
    fun bindTrendingResultMapper(obj: TrendingResultMapper): TrendingResult.Mapper<Unit>

    @Binds
    @TrendingScope
    fun bindToPlaylistUiMapper(obj: TopBarItemDomain.ToUiMapper): TopBarItemDomain.Mapper<TrendingTopBarItemUi>

    @Binds
    @[IntoMap ViewModelKey(TrendingViewModel::class)]
    fun bindTrendingViewModel(trendingViewModel: TrendingViewModel): ViewModel

}

@Module
class TrendingModuleProvides{

    @Provides
    @TrendingScope
    fun provideMusicDataService(
        client: OkHttpClient,
        builder: Retrofit.Builder,
        converterFactory: ExtendedGsonConverterFactory
    ): TrendingService {
        return builder
            .baseUrl(AppModule.baseDataUrl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(TrendingService::class.java)
    }

}