package com.kamancho.melisma.app.vkdto

import com.kamancho.melisma.userplaylists.data.cache.PlaylistCache

data class Response(
   private val owner_id: Int,
   private val playlist_id: Int
){


   fun map(playlistCache: PlaylistCache): PlaylistCache{
      return playlistCache.copy(playlistId = playlist_id.toString(), owner_id = owner_id)
   }
}