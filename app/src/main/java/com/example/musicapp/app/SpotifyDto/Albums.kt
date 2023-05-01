package com.example.musicapp.app.SpotifyDto

/**
 * Created by HP on 29.04.2023.
 **/
data class Albums(
    val href: String,
    val items: List<AlbumItem>,
    val limit: Int,
    val next: String,
    val offset: Int,
    val previous: String,
    val total: Int
)