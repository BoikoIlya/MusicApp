package com.example.musicapp.popular.di

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.ExtendedGsonConverterFactory
import com.example.musicapp.app.core.SearchQueryRepository
import com.example.musicapp.app.vkdto.SearchPlaylistItem
import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.main.di.AppModule
import com.example.musicapp.main.di.ViewModelKey
import com.example.musicapp.popular.data.PopularRepository
import com.example.musicapp.popular.data.PopularsService
import com.example.musicapp.popular.domain.PopularInteractor
import com.example.musicapp.popular.presentation.PopularCommunication
import com.example.musicapp.popular.presentation.PopularListCommunication
import com.example.musicapp.popular.presentation.PopularUiStateCommunication
import com.example.musicapp.popular.presentation.PopularViewModel
import com.example.musicapp.popular.presentation.TracksResultToPopularTracksCommunicationMapper
import com.example.musicapp.search.data.BaseSearchQueryRepository
import com.example.musicapp.search.data.SearchRepository
import com.example.musicapp.search.data.cloud.PlaylistsCloudToPlaylistUiMapper
import com.example.musicapp.search.data.cloud.SearchPlaylistsService
import com.example.musicapp.search.data.cloud.SearchTracksService
import com.example.musicapp.search.data.cloud.TracksCloudToMediaItemsMapper
import com.example.musicapp.search.presentation.BasePlaylistsListSearchViewModel
import com.example.musicapp.search.presentation.BaseTracksListSearchViewModel
import com.example.musicapp.search.presentation.SearchViewModel
import com.example.musicapp.trending.di.TrendingScope
import com.example.musicapp.trending.domain.TrackDomain
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import com.example.musicapp.userplaylists.presentation.PlaylistUi
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
    fun bindCloudTrackToTrackDomainMapper(obj: com.example.musicapp.app.vkdto.TrackItem.Mapper.CloudTrackToTrackDomainMapper): com.example.musicapp.app.vkdto.TrackItem.Mapper<TrackDomain>

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