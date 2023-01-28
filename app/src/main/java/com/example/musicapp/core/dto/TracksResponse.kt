package com.example.musicapp.core.dto

data class TracksResponse(
    val meta: MetaX,
    val tracks: List<Track>
)