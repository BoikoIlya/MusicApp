package com.example.musicapp.app.SpotifyDto

import com.example.musicapp.trending.domain.TrackDomain
import com.example.testapp.spotifyDto.Album
import com.example.testapp.spotifyDto.Artist
import com.example.testapp.spotifyDto.ExternalIds
import com.example.testapp.spotifyDto.ExternalUrls
import com.example.testapp.spotifyDto.LinkedFrom
import javax.inject.Inject

data class TrackItemPlaylist(
   private val album: AlbumPlaylist,
   private val artists: List<Artist>,
   private val available_markets: List<String>,
   private val disc_number: Int,
   private val duration_ms: Int,
   private val episode: Boolean,
   private val explicit: Boolean,
   private val external_ids: ExternalIds,
   private val external_urls: ExternalUrls,
   private val href: String,
   private val id: String,
   private val is_local: Boolean,
   private val name: String,
   private val popularity: Int,
   private val preview_url: String?,
   private val track: Boolean,
   private val track_number: Int,
   private val type: String,
   private val uri: String
){


    fun map(): Boolean = preview_url != null

    fun <T>map(mapper: Mapper<T>):T = mapper.map(
        album,
        artists,
        available_markets,
        disc_number,
        duration_ms,
        episode,
        explicit,
        external_ids,
        external_urls,
        href,
        id,
        is_local,
        name,
        popularity,
        preview_url,
        track,
        track_number,
        type,
        uri
    )

    interface Mapper<T>{

        fun map(
             album: AlbumPlaylist,
             artists: List<Artist>,
             available_markets: List<String>,
             disc_number: Int,
             duration_ms: Int,
             episode: Boolean,
             explicit: Boolean,
             external_ids: ExternalIds,
             external_urls: ExternalUrls,
             href: String,
             id: String,
             is_local: Boolean,
             name: String,
             popularity: Int,
             preview_url: String?,
             track: Boolean,
             track_number: Int,
             type: String,
             uri: String
        ): T
    }

    class ToTrackDomainMapper @Inject constructor(): Mapper<TrackDomain>{

        override fun map(
            album: AlbumPlaylist,
            artists: List<Artist>,
            available_markets: List<String>,
            disc_number: Int,
            duration_ms: Int,
            episode: Boolean,
            explicit: Boolean,
            external_ids: ExternalIds,
            external_urls: ExternalUrls,
            href: String,
            id: String,
            is_local: Boolean,
            name: String,
            popularity: Int,
            preview_url: String?,
            track: Boolean,
            track_number: Int,
            type: String,
            uri: String,
        ): TrackDomain {
            return TrackDomain(
                id =  id,
                imageUrl = album.images.first().url,
                name = name,
                artistName = artists.joinToString(separator = " & "){ it.name },
                previewURL = preview_url?:"",
                albumName = album.name,
            )
        }
    }
}