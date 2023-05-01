package com.example.musicapp.app.SpotifyDto

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.musicapp.trending.domain.TrackDomain
import com.example.testapp.spotifyDto.Album
import com.example.testapp.spotifyDto.Artist
import com.example.testapp.spotifyDto.ExternalIds
import com.example.testapp.spotifyDto.ExternalUrls

/**
 * Created by HP on 29.04.2023.
 **/
data class SearchItem(
    val album: Album,
    val artists: List<Artist>,
    val available_markets: List<String>,
    val disc_number: Int,
    val duration_ms: Int,
    val explicit: Boolean,
    val external_ids: ExternalIds,
    val external_urls: ExternalUrls,
    val href: String,
    val id: String,
    val is_local: Boolean,
    val name: String,
    val popularity: Int,
    val preview_url: String,
    val track_number: Int,
    val type: String,
    val uri: String
){

    fun map() = MediaItem.Builder()
        .setMediaId(preview_url)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(name)
                .setArtist(artists.joinToString(separator = " & "){ it.name })
                .setAlbumTitle(album.name)
                .setArtworkUri(Uri.parse(album.images.first().url))
                .build()
        )
        .build()
}
