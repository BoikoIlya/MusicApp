package com.example.musicapp.core.dto

data class Image(
    val contentId: String,
    val height: Int,
    val id: String,
    val imageType: String,
    val isDefault: Boolean,
    val type: String,
    val url: String,
    val version: Int,
    val width: Int
)