package com.kamancho.melisma.app.vkdto

data class FriendsResponse(
    val count: Int,
    val items: List<FriendItem>
)