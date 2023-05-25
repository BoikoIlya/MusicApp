package com.example.musicapp.app.SpotifyDto

import com.example.musicapp.playlist.data.PlaylistDataDomain
import com.example.musicapp.trending.domain.TrackDomain
import com.example.testapp.spotifyDto.ExternalUrls
import com.example.testapp.spotifyDto.Image
import javax.inject.Inject

data class PlaylistDto(
   private val collaborative: Boolean,
   private val description: String,
   private val external_urls: ExternalUrls,
   private val followers: Followers,
   private val href: String,
   private val id: String,
   private val images: List<Image>,
   private val name: String,
   private val owner: Owner,
   private val primary_color: Any,
   private val `public`: Boolean,
   private val snapshot_id: String,
   private val tracks: PlaylistTracks,
   private val type: String,
   private val uri: String
){

    fun <T>map(mapper: Mapper<T>):T = mapper.map(
        collaborative,
        description,
        external_urls,
        followers,
        href,
        id,
        images,
        name,
        owner,
        primary_color,
        public,
        snapshot_id,
        tracks,
        type,
        uri
    )

    interface Mapper<T>{

        fun map(
            collaborative: Boolean,
            description: String,
            external_urls: ExternalUrls,
            followers: Followers,
            href: String,
            id: String,
            images: List<Image>,
            name: String,
            owner: Owner,
            primary_color: Any,
            `public`: Boolean,
            snapshot_id: String,
            tracks: PlaylistTracks,
            type: String,
            uri: String
        ): T
    }

    class ToPlaylistDomainMapper @Inject constructor(
        private val mapper: TrackItemPlaylist.Mapper<TrackDomain>
    ): Mapper<PlaylistDataDomain> {

        override fun map(
            collaborative: Boolean,
            description: String,
            external_urls: ExternalUrls,
            followers: Followers,
            href: String,
            id: String,
            images: List<Image>,
            name: String,
            owner: Owner,
            primary_color: Any,
            public: Boolean,
            snapshot_id: String,
            tracks: PlaylistTracks,
            type: String,
            uri: String,
        ): PlaylistDataDomain {
            return PlaylistDataDomain(
                tracks = tracks.items.filter { it.track.map() }.map { it.track.map(mapper) },
                albumDescription = description,
                albumName = name ,
                albumImgUrl = images.first().url
            )
        }
    }

}