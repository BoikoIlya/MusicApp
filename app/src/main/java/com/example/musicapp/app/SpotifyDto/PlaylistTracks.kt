package com.example.musicapp.app.SpotifyDto

data class PlaylistTracks(
    val href: String,
    val items: List<PlaylistTrackItem>,
    val limit: Int,
    val next: Any,
    val offset: Int,
    val previous: Any,
    val total: Int
)