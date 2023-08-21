package com.example.musicapp.app.vkdto

import com.example.musicapp.app.vkdto.PlaylistItem

/**
 * Created by HP on 14.08.2023.
 **/
data class SearchPlaylistsResponse(
    val count: Int,
    val items: List<SearchPlaylistItem>
)
