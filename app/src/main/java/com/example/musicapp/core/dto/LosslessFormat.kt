package com.example.musicapp.core.dto

data class LosslessFormat(
    val bitrate: Int,
    val name: String,
    val sampleBits: Int,
    val sampleRate: Int,
    val type: String
)