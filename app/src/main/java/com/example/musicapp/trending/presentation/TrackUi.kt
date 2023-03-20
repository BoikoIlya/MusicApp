package com.example.musicapp.trending.presentation

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaController
import com.example.musicapp.R
import com.example.musicapp.main.presentation.PlayerControlsState
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.databinding.TrackItemBinding
import com.google.common.util.concurrent.ListenableFuture
import javax.inject.Inject

data class TrackUi(
    private val id: String,
    private val playbackMinutes: String,
    private val name: String,
    private val artistName: String,
    private val previewURL: String,
    private val albumName: String,
    private val bgColor: Int
){

    interface Mapper<T>{
        fun map(
            id: String,
            playbackMinutes: String,
            name: String,
            artistName: String,
            previewURL: String,
            albumName: String,
            bgColor: Int
        ): T
    }

    fun <T>map(mapper: Mapper<T>): T = mapper.map(
        id,
        playbackMinutes,
        name,
        artistName,
        previewURL,
        albumName,
        bgColor
    )

    class ListItemUi(
        private val binding: TrackItemBinding,
    ): Mapper<Unit>{
        override fun map(
            id: String,
            playbackMinutes: String,
            name: String,
            artistName: String,
            previewURL: String,
            albumName: String,
            bgColor: Int
        ) {
            with(binding){
                songNameTv.text = name
                authorNameTv.text = artistName//todo maybe remove class
                root.setBackgroundColor(bgColor)
            }
        }


    }

    class ChangeSelectedItemBg @Inject constructor(
        private val managerResource: ManagerResource
    ): Mapper<TrackUi>{
        override fun map(
            id: String,
            playbackMinutes: String,
            name: String,
            artistName: String,
            previewURL: String,
            albumName: String,
            bgColor: Int,
        ): TrackUi {
            return TrackUi(
                id = id,
                playbackMinutes = playbackMinutes,
                name = name,
                artistName = artistName,
                previewURL = previewURL,
                albumName = albumName,
                bgColor = managerResource.getColor(R.color.light_gray))

        }

    }


    fun map(item: TrackUi):Boolean =  id == item.id

    class ToPlayStateBottomBar @Inject constructor(): Mapper<PlayerControlsState.Play>{
        override fun map(
            id: String,
            playbackMinutes: String,
            name: String,
            artistName: String,
            previewURL: String,
            albumName: String,
            bgColor: Int,
        ): PlayerControlsState.Play {
            return PlayerControlsState.Play(MediaItem.Builder().build())
        }

    }

    class ToPlayStateService @Inject constructor(
       private val controllerFuture: ListenableFuture<MediaController>
    ): Mapper<Unit>{
        override fun map(
            id: String,
            playbackMinutes: String,
            name: String,
            artistName: String,
            previewURL: String,
            albumName: String,
            bgColor: Int,
        ): Unit {

            controllerFuture.get().setMediaItem(
                MediaItem.Builder()
                    .setMediaId(previewURL)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                        .setTitle(name)
                        .setArtist(artistName)
                            .setAlbumTitle(albumName)
                        .setArtworkUri(Uri.parse("android.resource://com.example.musicapp/drawable/notification_bg"))
                        .build())
                    .build()
            )
        }

    }

}
