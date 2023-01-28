package com.example.musicapp.trending.presentation

import java.lang.ref.SoftReference

data class TrackUi(
    private val id: String,
    private val playbackMinutes: String,
    private val name: String,
    private val artistName: String,
    private val previewURL: String,
    private val albumName: String
)
