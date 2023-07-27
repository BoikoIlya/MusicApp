package com.example.musicapp.app.vkdto



data class PlaylistsResponse(
    val count: Int,
    val groups: List<Any>,
    val items: List<PlaylistItem>,
    val next_from: String,
    val profiles: List<Any>
)