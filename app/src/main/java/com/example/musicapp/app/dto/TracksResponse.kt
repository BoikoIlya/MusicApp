package com.example.musicapp.app.dto

data class TracksResponse(
    val meta: MetaX,
    val tracks: List<Track>
)