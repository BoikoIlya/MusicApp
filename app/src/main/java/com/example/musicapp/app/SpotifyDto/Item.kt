package com.example.musicapp.app.SpotifyDto

import com.example.musicapp.trending.domain.PlaylistDomain
import com.example.testapp.spotifyDto.ExternalUrls
import com.example.testapp.spotifyDto.Image
import javax.inject.Inject

data class Item(
    val collaborative: Boolean,
    val description: String,
    val external_urls: ExternalUrls,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val owner: Owner,
    val primary_color: Any?,
    val `public`: Any?,
    val snapshot_id: String,
    val tracks: Tracks,
    val type: String,
    val uri: String
){

    fun <T> map(mapper: Mapper<T>):T = mapper.map(
        collaborative,
        description,
        external_urls,
        href,
        id,
        images,
        name,
        owner,
        primary_color,
        public,
        snapshot_id,
        tracks,
        type,
        uri
    )

    interface Mapper<T> {
        fun map(
            collaborative: Boolean,
            description: String,
            external_urls: ExternalUrls,
            href: String,
            id: String,
            images: List<Image>,
            name: String,
            owner: Owner,
            primary_color: Any?,
            `public`: Any?,
            snapshot_id: String,
            tracks: Tracks,
            type: String,
            uri: String
        ): T
    }

        class ToPlaylistsDomainMapper @Inject constructor(): Mapper<PlaylistDomain>{
            override fun map(
                collaborative: Boolean,
                description: String,
                external_urls: ExternalUrls,
                href: String,
                id: String,
                images: List<Image>,
                name: String,
                owner: Owner,
                primary_color: Any?,
                `public`: Any?,
                snapshot_id: String,
                tracks: Tracks,
                type: String,
                uri: String
            ): PlaylistDomain {
                return PlaylistDomain(
                    id = id,
                    name = name,
                    descriptions = description,
                    imgUrl = images.first().url,
                    tracksUrl = href
                )
            }

        }

}