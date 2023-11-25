package com.kamancho.melisma.search.di

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.ExtendedGsonConverterFactory
import com.kamancho.melisma.app.core.PagingSource
import com.kamancho.melisma.app.core.SearchQueryRepository
import com.kamancho.melisma.app.vkdto.SearchPlaylistItem
import com.kamancho.melisma.app.vkdto.TrackItem
import com.kamancho.melisma.main.di.AppModule
import com.kamancho.melisma.main.di.ViewModelKey
import com.kamancho.melisma.search.data.BaseSearchQueryRepository
import com.kamancho.melisma.search.data.SearchRepository
import com.kamancho.melisma.search.data.cloud.SearchCloudDataSource
import com.kamancho.melisma.search.data.cloud.SearchPlaylistsService
import com.kamancho.melisma.search.data.cloud.SearchTracksService
import com.kamancho.melisma.search.domain.SearchDomainState
import com.kamancho.melisma.search.domain.SearchInteractor
import com.kamancho.melisma.search.presentation.BasePlaylistsListSearchViewModel
import com.kamancho.melisma.search.presentation.BaseTracksListSearchViewModel
import com.kamancho.melisma.search.presentation.SearchDomainStateMapper
import com.kamancho.melisma.search.presentation.SearchListCommunication
import com.kamancho.melisma.search.presentation.SearchPagingLoadStateCommunicationPlaylists
import com.kamancho.melisma.search.presentation.SearchPagingLoadStateCommunicationTracks
import com.kamancho.melisma.search.presentation.SearchViewModel
import com.kamancho.melisma.trending.domain.TrackDomain
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
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
interface SearchModule {

    @Binds
    @SearchScope
    fun bindSearchDomainStateMapperBaseTracks(obj: SearchDomainStateMapper.BaseTracks): SearchDomainState.Mapper<TrackDomain>

    @Binds
    @SearchScope
    fun bindSearchDomainStateMapperBasePlaylists(obj: SearchDomainStateMapper.BasePlaylists): SearchDomainState.Mapper<PlaylistDomain>

    @Binds
    @Reusable
    fun bindSearchListCommunicationBaseTracks(obj: SearchListCommunication.BaseTracks): SearchListCommunication<MediaItem>

    @Binds
    @Reusable
    fun bindSearchListCommunicationBasePlaylists(obj: SearchListCommunication.BasePlaylists): SearchListCommunication<PlaylistUi>

    @Binds
    @Reusable
    fun bindSearchPagingLoadStateCommunicationBaseTracks(obj: SearchPagingLoadStateCommunicationTracks.Base): SearchPagingLoadStateCommunicationTracks

    @Binds
    @Reusable
    fun bindSearchPagingLoadStateCommunicationBasePlaylists(obj: SearchPagingLoadStateCommunicationPlaylists.Base): SearchPagingLoadStateCommunicationPlaylists

    @Binds
    @SearchScope
    fun bindSearchInteractorBaseTracks(obj: SearchInteractor.BaseTracks): SearchInteractor<TrackItem,TrackDomain>

    @Binds
    @SearchScope
    fun bindSearchInteractorBasePlaylists(obj: SearchInteractor.BasePlaylists): SearchInteractor<SearchPlaylistItem,PlaylistDomain>

    @Binds
    @SearchScope
    fun bindSearchCloudDataSourceBaseTracksSearch(obj: SearchCloudDataSource.BaseTracksSearch): SearchCloudDataSource<TrackItem>

    @Binds
    @SearchScope
    fun bindSearchCloudDataSourceBasePlaylistsSearch(obj: SearchCloudDataSource.BasePlaylistsSearch): SearchCloudDataSource<SearchPlaylistItem>

    @Binds
    @SearchScope
    fun bindCloudTrackToTrackDomainMapper(obj: TrackItem.Mapper.CloudTrackToTrackDomainMapper): TrackItem.Mapper<TrackDomain>


    @Binds
    @SearchScope
    fun bindToPlaylistDomainMapper(obj: SearchPlaylistItem.ToPlaylistDomainMapper): SearchPlaylistItem.Mapper<PlaylistDomain>

    @Binds
    @SearchScope
    fun bindToPlaylistUi(obj: PlaylistDomain.ToPlaylistUi): PlaylistDomain.Mapper<PlaylistUi>

    @Binds
    @SearchScope
    fun bindPagingSourceTracksPagingSource(obj: PagingSource.TracksPagingSource): PagingSource<TrackItem>

    @Binds
    @SearchScope
    fun bindPagingSourcePlaylistsPagingSource(obj: PagingSource.PlaylistsPagingSource): PagingSource<SearchPlaylistItem>












    @Binds
    @SearchScope
    fun bindSearchPlaylistsRepository(obj: SearchRepository.BasePlaylists): SearchRepository<SearchPlaylistItem>

    @Binds
    @SearchScope
    fun bindBaseSearchQueryRepository(obj: BaseSearchQueryRepository): SearchQueryRepository

    @Binds
    @SearchScope
    fun bindSearchTracksRepository(obj: SearchRepository.BaseTracks): SearchRepository<TrackItem>


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