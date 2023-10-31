package com.kamancho.melisma.searchplaylistdetails.di

import androidx.lifecycle.ViewModel
import com.kamancho.melisma.app.core.ExtendedGsonConverterFactory
import com.kamancho.melisma.app.core.FollowPlaylistInteractor
import com.kamancho.melisma.app.core.PlaylistDataInteractor
import com.kamancho.melisma.app.vkdto.SearchPlaylistItem
import com.kamancho.melisma.editplaylist.data.cloud.PlaylistTracksService
import com.kamancho.melisma.favoritesplaylistdetails.di.FavoritesPlaylistDetailsScope
import com.kamancho.melisma.main.di.AppModule

import com.kamancho.melisma.main.di.ViewModelKey
import com.kamancho.melisma.favoritesplaylistdetails.presentation.PlaylistDetailsCommunication
import com.kamancho.melisma.favoritesplaylistdetails.presentation.PlaylistDetailsStateCommunication
import com.kamancho.melisma.favoritesplaylistdetails.presentation.PlaylistDetailsTracksCommunication
import com.kamancho.melisma.favoritesplaylistdetails.presentation.PlaylistLoadingCommunication
import com.kamancho.melisma.searchplaylistdetails.data.SearchPlaylistDetailsRepository
import com.kamancho.melisma.searchplaylistdetails.data.TrackCacheToDomainMapper
import com.kamancho.melisma.searchplaylistdetails.data.cache.SearchPlaylistTracksCacheDataSource
import com.kamancho.melisma.searchplaylistdetails.domain.SearchPlaylistDetailsInteractor
import com.kamancho.melisma.searchplaylistdetails.presentation.BaseSearchPlaylistDetailsResult
import com.kamancho.melisma.searchplaylistdetails.presentation.SearchPlaylistDetailsViewModel
import com.kamancho.melisma.trending.presentation.SearchPlaylistDetailsResult
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import com.kamancho.melisma.userplaylists.presentation.PlaylistsResultUpdateToUiEventMapper
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
    fun bindPlaylistDomainToIdMapper(obj: PlaylistDomain.ToIdMapper): PlaylistDomain.Mapper<String>

    @Binds
    @SearchPlaylistDetailsScope
    fun bindPlaylistUiToOwnerIdMapper(obj: PlaylistUi.ToOwnerIdMapper): PlaylistUi.Mapper<Int>

    @Binds
    @SearchPlaylistDetailsScope
    fun bindPlaylistCloudToDomainMapper(obj: SearchPlaylistItem.ToPlaylistDomainMapper): SearchPlaylistItem.Mapper<PlaylistDomain>

    @Binds
    @SearchPlaylistDetailsScope
    fun bindPlaylistUiToTitleMapper(obj: PlaylistUi.ToTitleMapper.Base): PlaylistUi.ToTitleMapper

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