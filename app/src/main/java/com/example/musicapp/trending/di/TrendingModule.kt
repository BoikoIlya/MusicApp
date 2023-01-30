package com.example.musicapp.trending.di

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.musicapp.ImageLoader
import com.example.musicapp.app.HandleError
import com.example.musicapp.app.app.di.ViewModelKey
import com.example.musicapp.app.dto.Playlist
import com.example.musicapp.app.dto.Track
import com.example.musicapp.trending.data.HandleResponse
import com.example.musicapp.trending.data.TrendingRepository
import com.example.musicapp.trending.domain.PlaylistDomain
import com.example.musicapp.trending.domain.TrackDomain
import com.example.musicapp.trending.domain.TrendingInteractor
import com.example.musicapp.trending.presentation.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

/**
 * Created by HP on 28.01.2023.
 **/


@Module
interface TrendingModule{

    @Binds
    @TrendingScope
    fun bindImageLoaderForPlaylists(obj: ImageLoader.Base): ImageLoader

    @Binds
    @TrendingScope
    fun bindRepository(obj: TrendingRepository.Base): TrendingRepository

    @Binds
    @TrendingScope
    fun bindToPlaylistMapper(obj: Playlist.ToPlaylistDomain): Playlist.Mapper<PlaylistDomain>

    @Binds
    @TrendingScope
    fun bindToTrackMapper(obj: Track.ToTrackDomain): Track.Mapper<TrackDomain>

    @Binds
    @TrendingScope
    fun bindHandleResponse(obj: HandleResponse.Base): HandleResponse

    @Binds
    @TrendingScope
    fun bindTrendingInteractor(obj: TrendingInteractor.Base): TrendingInteractor

    @Binds
    @TrendingScope
    fun bindHandleError(obj: HandleError.Base): HandleError

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
    fun bindToPlaylistUiMapper(obj: PlaylistDomain.ToPlaylistUiMapper): PlaylistDomain.Mapper<PlaylistUi>

    @Binds
    @TrendingScope
    fun bindTrackUiMapper(obj: TrackDomain.ToTrackUiMapper): TrackDomain.Mapper<TrackUi>

    @Binds
    @[IntoMap ViewModelKey(TrendingViewModel::class)]
    fun bindTrendingViewModel(trendingViewModel: TrendingViewModel): ViewModel

}

