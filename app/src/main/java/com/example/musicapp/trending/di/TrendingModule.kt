package com.example.musicapp.trending.di

import androidx.lifecycle.ViewModel
import com.example.musicapp.main.di.ViewModelKey
import com.example.musicapp.trending.data.TrendingRepository
import com.example.musicapp.trending.domain.PlaylistDomain
import com.example.musicapp.trending.domain.TrackDomain
import com.example.musicapp.trending.domain.TrendingInteractor
import com.example.musicapp.trending.presentation.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by HP on 28.01.2023.
 **/


@Module
interface TrendingModule{



    @Binds
    @TrendingScope
    fun bindCloudTrackToTrackDomainMapper(obj: com.example.musicapp.app.vkdto.TrackItem.Mapper.CloudTrackToTrackDomainMapper): com.example.musicapp.app.vkdto.TrackItem.Mapper<TrackDomain>

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
    fun bindToPlaylistUiMapper(obj: PlaylistDomain.ToPlaylistUiMapper): PlaylistDomain.Mapper<PlaylistUi>

    @Binds
    @[IntoMap ViewModelKey(TrendingViewModel::class)]
    fun bindTrendingViewModel(trendingViewModel: TrendingViewModel): ViewModel

}

