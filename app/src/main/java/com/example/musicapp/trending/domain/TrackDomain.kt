package com.example.musicapp.trending.domain

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.musicapp.app.core.ManagerResource
import java.net.URL
import javax.inject.Inject

/**
 * Created by HP on 27.01.2023.
 **/

data class TrackDomain(
    private val id: String,
    private val imageUrl: String,
    private val name: String,
    private val artistName: String,
    private val previewURL: String,
    private val albumName: String
){
    interface Mapper<T>{
        fun map(
            id: String,
            imageUrl: String,
            name: String,
            artistName: String,
            previewURL: String,
            albumName: String
        ): T
    }

    fun <T>map(mapper: Mapper<T>): T  = mapper.map(id, imageUrl, name, artistName, previewURL, albumName)

    class ToTrackUiMapper @Inject constructor(): Mapper<MediaItem>{
        override fun map(
            id: String,
            imageUrl: String,
            name: String,
            artistName: String,
            previewURL: String,
            albumName: String
        ): MediaItem {

            return MediaItem.Builder()
                .setMediaId(previewURL)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(name)
                        .setArtist(artistName)
                        .setAlbumTitle(albumName)
                        .setArtworkUri(Uri.parse(imageUrl))
                        .build()
                        )
                .build()
        }

    }

}