package com.example.musicapp.app.SpotifyDto

import com.example.testapp.spotifyDto.Artist
import com.example.testapp.spotifyDto.ExternalIds
import com.example.testapp.spotifyDto.ExternalUrls
import com.example.testapp.spotifyDto.Image

/**
 * Created by HP on 29.04.2023.
 **/
data class AlbumItem(
    val album_group: String,
    val album_type: String,
    val artists: List<Artist>,
    val available_markets: List<String>,
    val copyrights: List<Copyright>,
    val external_ids: ExternalIds,
    val external_urls: ExternalUrls,
    val genres: List<String>,
    val href: String,
    val id: String,
    val images: List<Image>,
    val label: String,
    val name: String,
    val popularity: Int,
    val release_date: String,
    val release_date_precision: String,
    val restrictions: Restrictions,
    val total_tracks: Int,
    val type: String,
    val uri: String
)