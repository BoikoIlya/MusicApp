package com.example.musicapp.playlist.di

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem

import com.example.musicapp.app.core.HandleResponse
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.main.di.ViewModelKey
import com.example.musicapp.playlist.data.PlaylistDataDomain
import com.example.musicapp.playlist.data.PlaylistRepository
import com.example.musicapp.playlist.domain.PlaylistInteractor
import com.example.musicapp.playlist.presentation.AdditionalPlaylistInfoCommunication
import com.example.musicapp.playlist.presentation.PlaylistCommunication
import com.example.musicapp.playlist.presentation.PlaylistStateCommunication
import com.example.musicapp.playlist.presentation.PlaylistTracksCommunication
import com.example.musicapp.playlist.presentation.PlaylistViewModel
import com.example.musicapp.playlist.presentation.TracksResultToPlaylistTracksCommunicationMapper
import com.example.musicapp.trending.domain.TrackDomain
import com.example.musicapp.trending.presentation.TrendingResult
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by HP on 23.04.2023.
 **/
@Module
interface PlaylistModule {


    @PlaylistScope
    @Binds
    fun bindPlaylistRepository(obj: PlaylistRepository.Base):
            PlaylistRepository


    @PlaylistScope
    @Binds
    fun bindPlaylistInteractor(obj: PlaylistInteractor.Base):
            PlaylistInteractor

    @PlaylistScope
    @Binds
    fun bindToTrackResultMapper(obj: PlaylistDataDomain.ToTrackResultMapper):
            PlaylistDataDomain.Mapper<TracksResult>


    @PlaylistScope
    @Binds
    fun bindAdditionalPlaylistInfoCommunication(obj: AdditionalPlaylistInfoCommunication.Base):
            AdditionalPlaylistInfoCommunication

    @PlaylistScope
    @Binds
    fun bindPlaylistStateCommunication(obj: PlaylistStateCommunication.Base):
            PlaylistStateCommunication

    @PlaylistScope
    @Binds
    fun bindPlaylistTracksCommunication(obj: PlaylistTracksCommunication.Base):
            PlaylistTracksCommunication

    @PlaylistScope
    @Binds
    fun bindPlaylistCommunication(obj: PlaylistCommunication.Base):
            PlaylistCommunication

    @PlaylistScope
    @Binds
    fun bindTracksResultToPlaylistTracksCommunicationMapper(obj: TracksResultToPlaylistTracksCommunicationMapper.Base):
            TracksResultToPlaylistTracksCommunicationMapper

    @Binds
    @[IntoMap ViewModelKey(PlaylistViewModel::class)]
    fun bindQueueViewModel(queueViewModel: PlaylistViewModel): ViewModel
}