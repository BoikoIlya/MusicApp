package com.example.musicapp.app.SpotifyDto

import com.example.musicapp.trending.domain.PlaylistDomain

data class Playlists(
    val href: String,
    val items: List<Item>,
    val limit: Int,
    val next: Any,
    val offset: Int,
    val previous: String,
    val total: Int
)