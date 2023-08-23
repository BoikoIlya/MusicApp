package com.kamancho.melisma.app.vkdto

/**
 * Created by HP on 14.08.2023.
 **/
data class SearchPlaylistsResponse(
    val count: Int,
    val items: List<SearchPlaylistItem>
)
