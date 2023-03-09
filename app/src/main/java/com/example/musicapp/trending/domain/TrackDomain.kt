package com.example.musicapp.trending.domain

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.musicapp.R
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.trending.presentation.TrackUi
import javax.inject.Inject

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

    class ToTrackUiMapper @Inject constructor(
        private val managerResource: ManagerResource
    ): Mapper<MediaItem>{
        override fun map(
            id: String,
            playbackMinutes: String,
            name: String,
            artistName: String,
            previewURL: String,
            albumName: String
        ): MediaItem {
//            return TrackUi(
//                id = id,
//                playbackMinutes = playbackMinutes,
//                name = name,
//                artistName = artistName,
//                previewURL = previewURL,
//                albumName = albumName,
//                bgColor = managerResource.getColor(R.color.white)
//            )

            return MediaItem.Builder()
                .setMediaId(previewURL)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(name)
                        .setArtist(artistName)
                        .setAlbumTitle(albumName)
                        .setArtworkUri(Uri.parse("android.resource://com.example.musicapp/drawable/notification_bg"))
                        .setDescription(playbackMinutes)
                        .build()
                        )
                .build()
        }

    }

}