package com.example.musicapp.core.dto

import com.example.musicapp.trending.domain.PlaylistDomain

data class Playlist(
    val description: String,
    val favoriteCount: Int,
    val freePlayCompliant: Boolean,
    val href: String,
    val id: String,
    val images: List<Image>,
    val links: Links,
    val modified: String,
    val name: String,
    val privacy: String,
    val trackCount: Int,
    val type: String
) {
    interface Mapper<T> {

        fun map(
            description: String,
            favoriteCount: Int,
            freePlayCompliant: Boolean,
            href: String,
            id: String,
            images: List<Image>,
            links: Links,
            modified: String,
            name: String,
            privacy: String,
            trackCount: Int,
            type: String
        ): T
    }

    fun <T> map(mapper: Mapper<T>): T = mapper.map(
        description,
        favoriteCount,
        freePlayCompliant,
        href,
        id,
        images,
        links,
        modified,
        name,
        privacy,
        trackCount,
        type
    )


    class ToPlaylistDomain : Mapper<PlaylistDomain> {
        override fun map(
            description: String,
            favoriteCount: Int,
            freePlayCompliant: Boolean,
            href: String,
            id: String,
            images: List<Image>,
            links: Links,
            modified: String,
            name: String,
            privacy: String,
            trackCount: Int,
            type: String,
        ): PlaylistDomain {
            return PlaylistDomain(
                id = id,
                name = name,
                descriptions = description,
                imgUrl = images.first().url,
                tracksUrl = links.tracks.href
            )
        }


    }
}