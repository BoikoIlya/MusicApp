package com.kamancho.melisma.app.vkdto

data class Album(
    val access_key: String,
    val id: Int,
    val owner_id: Int,
    val thumb: TrackThumb?=null,
    val title: String
)