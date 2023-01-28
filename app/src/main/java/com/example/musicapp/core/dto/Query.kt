package com.example.musicapp.core.dto

data class Query(
    val limit: Int,
    val next: String,
    val offset: Int
)