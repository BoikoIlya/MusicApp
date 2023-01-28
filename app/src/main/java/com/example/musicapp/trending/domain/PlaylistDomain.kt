package com.example.musicapp.trending.domain

import com.example.musicapp.trending.presentation.PlaylistUi

/**
 * Created by HP on 27.01.2023.
 **/
data class PlaylistDomain(
    private val id: String,
    private val name: String,
    private val descriptions: String,
    private val imgUrl: String,
    private val tracksUrl: String
){

    interface Mapper<T>{
        fun map(
            id: String,
            name: String,
            descriptions: String,
            imgUrl: String,
            tracksUrl: String
        ): T
    }

    fun <T>map(mapper: Mapper<T>): T = mapper.map(id,name,descriptions,imgUrl,tracksUrl)

    class ToPlaylistUiMapper: Mapper<PlaylistUi> {

        override fun map(
            id: String,
            name: String,
            descriptions: String,
            imgUrl: String,
            tracksUrl: String,
        ): PlaylistUi {
            return PlaylistUi(
                id = id,
                name = name,
                descriptions = descriptions,
                imgUrl = imgUrl,
                tracksUrl = tracksUrl
            )
        }
    }

}

