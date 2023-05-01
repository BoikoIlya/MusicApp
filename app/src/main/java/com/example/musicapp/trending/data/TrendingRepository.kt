package com.example.musicapp.trending.data

import com.example.musicapp.app.SpotifyDto.Item
import com.example.musicapp.main.data.cache.TokenStore
import com.example.musicapp.main.data.cloud.TrendingService
import com.example.musicapp.trending.domain.PlaylistDomain
import com.example.musicapp.trending.domain.TrackDomain
import com.example.testapp.spotifyDto.Track
import java.util.Calendar
import javax.inject.Inject

/**
 * Created by HP on 27.01.2023.
 **/

interface TrendingRepository{

    suspend fun fetchPlaylists(): List<PlaylistDomain>

    suspend fun fetchTracks(): List<TrackDomain>

    class Base @Inject constructor(
        private val service: TrendingService,
        private val toPlaylistDomainMapper:Item.Mapper<PlaylistDomain>,
        private val toTrackDomain: Track.Mapper<TrackDomain>,
        private val token: TokenStore
    ): TrendingRepository {

        override suspend fun fetchPlaylists(): List<PlaylistDomain> =
            service.getFeaturedPlaylists(
               auth =  token.read(),
                timestamps = Calendar.getInstance().time.toString()
            ).playlists.items.map { it.map(toPlaylistDomainMapper) }


        override suspend fun fetchTracks(): List<TrackDomain> =
            service.getRecommendations(token.read(), market = "ES", seed_genres = "classical,country").tracks
                .filter { it.map() }.map { it.map(toTrackDomain) } //TODO HARDCODED STRINGS


    }

}