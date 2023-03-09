package com.example.musicapp.trending.data

import com.example.musicapp.app.dto.Playlist
import com.example.musicapp.app.dto.Track
import com.example.musicapp.app.main.data.TemporaryTracksCache
import com.example.musicapp.trending.data.cloud.TrendingService
import com.example.musicapp.trending.domain.PlaylistDomain
import com.example.musicapp.trending.domain.TrackDomain
import javax.inject.Inject

/**
 * Created by HP on 27.01.2023.
 **/

interface TrendingRepository{

    suspend fun fetchPlaylists(): List<PlaylistDomain>

    suspend fun fetchTracks(): List<TrackDomain>

    class Base @Inject constructor(
        private val service: TrendingService,
        private val toPlaylistDomainMapper:Playlist.Mapper<PlaylistDomain>,
        private val toTrackDomain: Track.Mapper<TrackDomain>,
        private val handleResponse: HandleResponse,
        private val tempCache: TemporaryTracksCache
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