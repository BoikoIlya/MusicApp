package com.example.musicapp.app.SpotifyDto

import com.example.testapp.spotifyDto.ExternalUrls
import com.example.testapp.spotifyDto.Image

/**
 * Created by HP on 29.04.2023.
 **/
data class ArtistItem(
    val external_urls: ExternalUrls,
    val followers: Followers,
    val genres: List<String>,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val popularity: Int,
    val type: String,
    val uri: String
)
