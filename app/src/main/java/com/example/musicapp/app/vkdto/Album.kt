package com.example.musicapp.app.vkdto

data class Album(
    val access_key: String,
    val id: Int,
    val owner_id: Int,
    val thumb: Thumb?=null,
    val title: String
)