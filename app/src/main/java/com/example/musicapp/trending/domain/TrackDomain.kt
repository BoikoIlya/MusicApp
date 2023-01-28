package com.example.musicapp.trending.domain

import com.example.musicapp.trending.presentation.TrackUi

/**
 * Created by HP on 27.01.2023.
 **/

data class TrackDomain(
    private val id: String,
    private val playbackMinutes: String,
    private val name: String,
    private val artistName: String,
    private val previewURL: String,
    private val albumName: String
){
    interface Mapper<T>{
        fun map(
            id: String,
            playbackMinutes: String,
            name: String,
            artistName: String,
            previewURL: String,
            albumName: String
        ): T
    }

    fun <T>map(mapper: Mapper<T>): T  = mapper.map(id, playbackMinutes, name, artistName, previewURL, albumName)

    class ToTrackUiMapper: Mapper<TrackUi>{
        override fun map(
            id: String,
            playbackMinutes: String,
            name: String,
            artistName: String,
            previewURL: String,
            albumName: String
        ): TrackUi {
            return TrackUi(
                id = id,
                playbackMinutes = playbackMinutes,
                name = name,
                artistName = artistName,
                previewURL = previewURL,
                albumName = albumName
            )
        }

    }
}