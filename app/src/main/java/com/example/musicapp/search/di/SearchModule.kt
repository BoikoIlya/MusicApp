package com.example.musicapp.search.di

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.ExtendedGsonConverterFactory
import com.example.musicapp.app.core.SearchQueryRepository
import com.example.musicapp.app.vkdto.SearchPlaylistItem
import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.main.di.AppModule
import com.example.musicapp.main.di.ViewModelKey
import com.example.musicapp.search.data.BaseSearchQueryRepository
import com.example.musicapp.search.data.SearchRepository
import com.example.musicapp.search.data.cloud.PlaylistsCloudToPlaylistUiMapper
import com.example.musicapp.search.data.cloud.SearchPlaylistsService
import com.example.musicapp.search.data.cloud.SearchTracksService
import com.example.musicapp.search.data.cloud.TracksCloudToMediaItemsMapper
import com.example.musicapp.search.presentation.BasePlaylistsListSearchViewModel
import com.example.musicapp.search.presentation.BaseTracksListSearchViewModel
import com.example.musicapp.search.presentation.SearchViewModel
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Created by HP on 01.05.2023.
 **/
@Module
interface SearchModule {

    @Binds
    @SearchScope
    fun bindSearchPlaylistsRepository(obj: SearchRepository.BasePlaylists): SearchRepository<PlaylistUi>

    @Binds
    @SearchScope
    fun bindBaseSearchQueryRepository(obj: BaseSearchQueryRepository): SearchQueryRepository

    @Binds
    @SearchScope
    fun bindSearchTracksRepository(obj: SearchRepository.BaseTracks): SearchRepository<MediaItem>

    @Binds
    @SearchScope
    fun bindCloudTrackToMediaItemMapper(obj: TrackItem.Mapper.CloudTrackToMediaItemMapper): TrackItem.Mapper<MediaItem>

    @Binds
    @SearchScope
    fun bindTracksCloudToMediaItemsMapper(obj: TracksCloudToMediaItemsMapper.Base): TracksCloudToMediaItemsMapper


    @Binds
    @SearchScope
    fun bindPlaylistsCloudToPlaylistUiMapper(obj: PlaylistsCloudToPlaylistUiMapper.Base): PlaylistsCloudToPlaylistUiMapper

    @Binds
    @SearchScope
    fun bindSearchPlaylistsCloudToPlaylistUiMapper(obj: SearchPlaylistItem.ToPlaylistUiMapper): SearchPlaylistItem.Mapper<PlaylistUi>

    @Binds
    @SearchScope
    fun bindPlaylistsUiToDomainMapper(obj: PlaylistUi.ToDomainMapper): PlaylistUi.Mapper<PlaylistDomain>

    @Binds
    @[IntoMap ViewModelKey(BasePlaylistsListSearchViewModel::class)]
    fun bindBasePlaylistsSearchViewModel(basePlaylistsSearchViewModel: BasePlaylistsListSearchViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(BaseTracksListSearchViewModel::class)]
    fun bindBaseTracksSearchViewModel(baseTracksSearchViewModel: BaseTracksListSearchViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(SearchViewModel::class)]
    fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

}


@Module
class SearchModuleProvides{

    @Provides
    @SearchScope
    fun provideSearchTracksService(
        client: OkHttpClient,
        builder: Retrofit.Builder,
        converterFactory: ExtendedGsonConverterFactory
    ): SearchTracksService {
        return builder
            .baseUrl(AppModule.baseDataUrl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(SearchTracksService::class.java)
    }

    @Provides
    @SearchScope
    fun provideSearchPlaylistsService(
        client: OkHttpClient,
        builder: Retrofit.Builder,
        converterFactory: ExtendedGsonConverterFactory
    ): SearchPlaylistsService {
        return builder
            .baseUrl(AppModule.baseDataUrl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(SearchPlaylistsService::class.java)
    }


}