package com.example.testapp.spotifyDto

import com.example.musicapp.trending.domain.TrackDomain
import javax.inject.Inject

data class Track(
   private val album: Album,
   private val artists: List<Artist>,
   private val disc_number: Int,
   private val duration_ms: Int,
   private val explicit: Boolean,
   private val external_ids: ExternalIds,
   private val external_urls: ExternalUrls,
   private val href: String,
   private val id: String,
   private val is_local: Boolean,
   private val is_playable: Boolean,
   private val linked_from: LinkedFrom?,
   private val name: String,
   private val popularity: Int,
   private val preview_url: String?,
   private val track_number: Int,
   private val type: String,
   private val uri: String
){

    fun map(): Boolean = preview_url != null

    fun <T>map(mapper: Mapper<T>):T = mapper.map(
        album,
        artists,
        disc_number,
        duration_ms,
        explicit,
        external_ids,
        external_urls,
        href,
        id,
        is_local,
        is_playable,
        linked_from,
        name,
        popularity,
        preview_url,
        track_number,
        type,
        uri
    )

    interface Mapper<T>{

        fun map(
            album: Album,
            artists: List<Artist>,
            disc_number: Int,
            duration_ms: Int,
            explicit: Boolean,
            external_ids: ExternalIds,
            external_urls: ExternalUrls,
            href: String,
            id: String,
            is_local: Boolean,
            is_playable: Boolean,
            linked_from: LinkedFrom?,
            name: String,
            popularity: Int,
            preview_url: String?,
            track_number: Int,
            type: String,
            uri: String
        ): T
    }

    class ToTrackDomainMapper @Inject constructor(): Mapper<TrackDomain>{
        override fun map(
            album: Album,
            artists: List<Artist>,
            disc_number: Int,
            duration_ms: Int,
            explicit: Boolean,
            external_ids: ExternalIds,
            external_urls: ExternalUrls,
            href: String,
            id: String,
            is_local: Boolean,
            is_playable: Boolean,
            linked_from: LinkedFrom?,
            name: String,
            popularity: Int,
            preview_url: String?,
            track_number: Int,
            type: String,
            uri: String,
        ): TrackDomain {
            return TrackDomain(
                id =  id,
                imageUrl = album.images.first().url,
                name = name,
                artistName = artists.joinToString(separator = " "){ it.name },
                previewURL = preview_url?:"",
                albumName = album.name,
            )
        }
    }


}