package com.example.musicapp.trending.data

import com.example.musicapp.core.dto.Playlist
import com.example.musicapp.core.dto.Track
import com.example.musicapp.core.dto.TracksResponse
import com.example.musicapp.trending.data.cloud.TrendingService
import com.example.musicapp.trending.domain.PlaylistDomain
import com.example.musicapp.trending.domain.TrackDomain

/**
 * Created by HP on 27.01.2023.
 **/

interface TrendingRepository{

    suspend fun fetchPlaylists(): List<PlaylistDomain>

    suspend fun fetchTracks(): List<TrackDomain>

    class Base(
        private val service: TrendingService,
        private val toPlaylistDomainMapper:Playlist.Mapper<PlaylistDomain>,
        private val toTrackDomain: Track.Mapper<TrackDomain>,
        private val handleResponse: HandleResponse
    ): TrendingRepository {

        override suspend fun fetchPlaylists(): List<PlaylistDomain> =
            handleResponse.handle {
                service.fetchPlaylists().playlists.map { it.map(toPlaylistDomainMapper) }
            }


        override suspend fun fetchTracks(): List<TrackDomain> =
            handleResponse.handle {
                service.fetchTop50Tracks().tracks.map { it.map(toTrackDomain) }
            }


    }

}