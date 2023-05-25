package com.example.musicapp.app.SpotifyDto

import com.example.testapp.spotifyDto.Artist
import com.example.testapp.spotifyDto.ExternalUrls
import com.example.testapp.spotifyDto.Image

data class AlbumPlaylist(
    val album_type: String,
    val artists: List<Artist>,
    val available_markets: List<String>,
    val external_urls: ExternalUrls,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val release_date: String,
    val release_date_precision: String,
    val total_tracks: Int,
    val type: String,
    val uri: String
)