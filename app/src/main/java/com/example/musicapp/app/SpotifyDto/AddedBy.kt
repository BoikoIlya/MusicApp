package com.example.musicapp.app.SpotifyDto

import com.example.testapp.spotifyDto.ExternalUrls

data class AddedBy(
    val external_urls: ExternalUrls,
    val href: String,
    val id: String,
    val type: String,
    val uri: String
)