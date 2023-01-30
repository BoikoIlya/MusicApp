package com.example.musicapp.app.dto

data class Query(
    val limit: Int,
    val next: String,
    val offset: Int
)