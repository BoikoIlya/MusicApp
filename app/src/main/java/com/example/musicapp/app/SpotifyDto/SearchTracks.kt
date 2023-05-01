package com.example.musicapp.app.SpotifyDto

import androidx.media3.common.MediaItem
import com.example.musicapp.trending.domain.TrackDomain
import com.example.testapp.spotifyDto.ExternalUrls
import com.example.testapp.spotifyDto.Image
import javax.inject.Inject

/**
 * Created by HP on 29.04.2023.
 **/
data class SearchTracks(
   private val href: String,
   private val items: List<SearchItem>,
   private val limit: Int,
   private val next: String,
   private val offset: Int,
   private val previous: Any,
   private val total: Int
){
    fun <T> map(mapper: Mapper<T>):T = mapper.map(
        href,
        items,
        limit,
        next,
        offset,
        previous,
        total
    )

    interface Mapper<T> {
        fun map(
            href: String,
            items: List<SearchItem>,
            limit: Int,
            next: String,
            offset: Int,
            previous: Any,
            total: Int
        ): T
    }


    class SearchTracksToMediaItemMapper @Inject constructor(): Mapper<List<MediaItem>>{
        override fun map(
            href: String,
            items: List<SearchItem>,
            limit: Int,
            next: String,
            offset: Int,
            previous: Any,
            total: Int,
        ): List<MediaItem> {
            return items.filter { it.preview_url.isNotEmpty() }.map { it.map() }
        }

    }
}
