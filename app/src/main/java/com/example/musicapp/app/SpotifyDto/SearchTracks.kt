package com.example.musicapp.app.SpotifyDto

import android.util.Log
import androidx.media3.common.MediaItem
import com.example.musicapp.main.data.TemporaryTracksCache
import javax.inject.Inject


/**
 * Created by HP on 29.04.2023.
 **/
data class SearchTracks(
   private val href: String,
   private val items: List<SearchItem>,
   private val limit: Int,
   private val next: String?,
   private val offset: Int,
   private val previous: Any,
   private val total: Int
){

    fun isEmpty() = items.isEmpty()
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
            next: String?,
            offset: Int,
            previous: Any,
            total: Int
        ): T
    }


    class Base @Inject constructor(): Mapper<List<MediaItem>>{
        override fun map(
            href: String,
            items: List<SearchItem>,
            limit: Int,
            next: String?,
            offset: Int,
            previous: Any,
            total: Int,
        ): List<MediaItem> {
            val tracks = items.filter { it.preview_url.isNotEmpty() }.map { it.map() }.distinctBy { it.mediaId }
            return tracks
        }

    }
}
