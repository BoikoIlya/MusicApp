package com.example.musicapp.searchplaylistdetails.di

import androidx.lifecycle.ViewModel
import com.example.musicapp.app.core.ExtendedGsonConverterFactory
import com.example.musicapp.app.core.FollowPlaylistInteractor
import com.example.musicapp.app.core.PlaylistDataInteractor
import com.example.musicapp.app.vkdto.SearchPlaylistItem
import com.example.musicapp.editplaylist.data.cloud.PlaylistTracksService
import com.example.musicapp.main.di.AppModule

import com.example.musicapp.main.di.ViewModelKey
import com.example.musicapp.favoritesplaylistdetails.presentation.PlaylistDetailsCommunication
import com.example.musicapp.favoritesplaylistdetails.presentation.PlaylistDetailsStateCommunication
import com.example.musicapp.favoritesplaylistdetails.presentation.PlaylistDetailsTracksCommunication
import com.example.musicapp.favoritesplaylistdetails.presentation.PlaylistLoadingCommunication
import com.example.musicapp.searchplaylistdetails.data.SearchPlaylistDetailsRepository
import com.example.musicapp.searchplaylistdetails.data.TrackCacheToDomainMapper
import com.example.musicapp.searchplaylistdetails.data.cache.SearchPlaylistTracksCacheDataSource
import com.example.musicapp.searchplaylistdetails.domain.SearchPlaylistDetailsInteractor
import com.example.musicapp.searchplaylistdetails.presentation.BaseSearchPlaylistDetailsResult
import com.example.musicapp.searchplaylistdetails.presentation.SearchPlaylistDetailsViewModel
import com.example.musicapp.trending.presentation.SearchPlaylistDetailsResult
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import com.example.musicapp.userplaylists.presentation.PlaylistsResultUpdateToUiEventMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Created by HP on 23.04.2023.
 **/
@Module
interface SearchPlaylistDetailsModule {



    @Binds
    @SearchPlaylistDetailsScope
    fun bindPlaylistCloudToDomainMapper(obj: SearchPlaylistItem.ToPlaylistDomainMapper): SearchPlaylistItem.Mapper<PlaylistDomain>

    @Binds
    @SearchPlaylistDetailsScope
    fun bindPlaylistDomainToTitleMapper(obj: PlaylistDomain.ToTitleMapper.Base): PlaylistDomain.ToTitleMapper

    @Binds
    @SearchPlaylistDetailsScope
    fun bindPlaylistDataInteractor(obj: PlaylistDataInteractor): FollowPlaylistInteractor


    @Binds
    @SearchPlaylistDetailsScope
    fun bindTrackCacheToDomainMapper(obj: TrackCacheToDomainMapper.Base): TrackCacheToDomainMapper

    @Binds
    @SearchPlaylistDetailsScope
    fun bindSearchPlaylistTracksCacheDataSource(obj: SearchPlaylistTracksCacheDataSource.Base): SearchPlaylistTracksCacheDataSource

    @Binds
    @SearchPlaylistDetailsScope
    fun bindCachedTracksRepositoryBaseMediaItem(obj: SearchPlaylistDetailsRepository.Base): SearchPlaylistDetailsRepository

    @Reusable
    @Binds
    fun bindPlaylistLoadingCommunication(obj: PlaylistLoadingCommunication.Base):
            PlaylistLoadingCommunication

    @SearchPlaylistDetailsScope
    @Binds
    fun bindSearchPlaylistDetailsInteractor(obj: SearchPlaylistDetailsInteractor.Base):
        SearchPlaylistDetailsInteractor


    @SearchPlaylistDetailsScope
    @Binds
    fun bindBaseSearchPlaylistDetailsResult(obj: BaseSearchPlaylistDetailsResult):
            SearchPlaylistDetailsResult.Mapper<Unit>

    @SearchPlaylistDetailsScope
    @Binds
    fun bindPlaylistsResultUpdateToUiEventMapper(obj: PlaylistsResultUpdateToUiEventMapper.Base):
            PlaylistsResultUpdateToUiEventMapper

    @Reusable
    @Binds
    fun bindPlaylistStateCommunication(obj: PlaylistDetailsStateCommunication.Base):
            PlaylistDetailsStateCommunication



    @Reusable
    @Binds
    fun bindPlaylistTracksCommunication(obj: PlaylistDetailsTracksCommunication.Base):
            PlaylistDetailsTracksCommunication

    @SearchPlaylistDetailsScope
    @Binds
    fun bindPlaylistCommunication(obj: PlaylistDetailsCommunication.Base):
            PlaylistDetailsCommunication

    @Binds
    @[IntoMap ViewModelKey(SearchPlaylistDetailsViewModel::class)]
    fun bindSearchPlaylistDetailsViewModel(searchPlaylistDetailsViewModel: SearchPlaylistDetailsViewModel): ViewModel
}

@Module
class SearchPlaylistDetailsProvidesModule{



    @Provides
    @SearchPlaylistDetailsScope
    fun providePlaylistTracksService(
        client: OkHttpClient,
        builder: Retrofit.Builder,
        converterFactory: ExtendedGsonConverterFactory
    ): PlaylistTracksService {
        return builder
            .baseUrl(AppModule.baseDataUrl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(PlaylistTracksService::class.java)
    }
}