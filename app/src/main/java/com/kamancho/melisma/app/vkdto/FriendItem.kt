package com.kamancho.melisma.app.vkdto

import com.kamancho.melisma.friends.data.cache.FriendCache

data class FriendItem(
   private val can_access_closed: Boolean,
   private val first_name: String,
   private val id: Int,
   private val is_closed: Boolean,
   private val photo_100: String,
   private val last_name: String,
   private val track_code: String,
    val deactivated:String?=null
){

   fun isDeactivated(): Boolean = deactivated!=null

   fun map(): FriendCache{
      return FriendCache(
         friendId = id,
         firstName = first_name,
         secondName = last_name,
         photoUrl = photo_100
      )
   }

}