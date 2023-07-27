package com.example.musicapp.app.core

import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.big_img_url
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.date
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.owner_id
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.small_img_url
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.track_duration_formatted
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.track_duration_in_millis
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.track_id
import com.example.musicapp.creteplaylist.presentation.SelectedTracksStore

/**
 * Created by HP on 26.07.2023.
 **/
interface CopyMediaItemMapper: Mapper<MediaItem,MediaItem> {

    class Base(
        private val selectedTrackBackgroundColor:Int,
        private val selectedTrackIconVisibility:Int,
    ): CopyMediaItemMapper {

        override fun map(data: MediaItem): MediaItem {
            val extraData = Bundle()
            extraData.putString(ToMediaItemMapper.track_duration_formatted,
                data.mediaMetadata.extras?.getString(track_duration_formatted)

            )
            extraData.putString(ToMediaItemMapper.big_img_url,
                data.mediaMetadata.extras?.getString(big_img_url)
            )
            extraData.putString(ToMediaItemMapper.small_img_url,
                data.mediaMetadata.extras?.getString(small_img_url)
            )
            extraData.putInt(ToMediaItemMapper.track_id,
                data.mediaMetadata.extras!!.getInt(track_id)
            )
            extraData.putInt(ToMediaItemMapper.date,
                data.mediaMetadata.extras!!.getInt(date)
            )
            extraData.putInt(ToMediaItemMapper.owner_id,
                data.mediaMetadata.extras!!.getInt(owner_id)
            )
            extraData.putFloat(ToMediaItemMapper.track_duration_in_millis,
                data.mediaMetadata.extras!!.getFloat(track_duration_in_millis)
            )

            return MediaItem.Builder()
                .setMediaId(data.mediaId)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(data.mediaMetadata.title)
                        .setArtist(data.mediaMetadata.artist)
                        .setAlbumTitle(data.mediaMetadata.albumTitle)
                        .setArtworkUri(data.mediaMetadata.artworkUri)
                        .setExtras(extraData)
                        .setIsPlayable(data.mediaMetadata.isPlayable)
                        .build()
                )
                .build()
        }
    }
}