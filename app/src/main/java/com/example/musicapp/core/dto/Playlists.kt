package com.example.musicapp.core.dto

import com.example.musicapp.trending.domain.PlaylistDomain

data class Playlists(
   val meta: Meta,
   val playlists: List<Playlist>
)