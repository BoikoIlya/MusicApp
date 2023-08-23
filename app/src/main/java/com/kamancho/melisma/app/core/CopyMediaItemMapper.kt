package com.kamancho.melisma.app.core

import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.kamancho.melisma.app.core.ToMediaItemMapper.Base.Companion.big_img_url
import com.kamancho.melisma.app.core.ToMediaItemMapper.Base.Companion.date
import com.kamancho.melisma.app.core.ToMediaItemMapper.Base.Companion.owner_id
import com.kamancho.melisma.app.core.ToMediaItemMapper.Base.Companion.small_img_url
import com.kamancho.melisma.app.core.ToMediaItemMapper.Base.Companion.track_duration_formatted
import com.kamancho.melisma.app.core.ToMediaItemMapper.Base.Companion.track_duration_in_millis
import com.kamancho.melisma.app.core.ToMediaItemMapper.Base.Companion.track_id

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
            extraData.putString(track_duration_formatted,
                data.mediaMetadata.extras?.getString(track_duration_formatted)

            )
            extraData.putString(big_img_url,
                data.mediaMetadata.extras?.getString(big_img_url)
            )
            extraData.putString(small_img_url,
                data.mediaMetadata.extras?.getString(small_img_url)
            )
            extraData.putInt(track_id,
                data.mediaMetadata.extras!!.getInt(track_id)
            )
            extraData.putInt(date,
                data.mediaMetadata.extras!!.getInt(date)
            )
            extraData.putInt(owner_id,
                data.mediaMetadata.extras!!.getInt(owner_id)
            )
            extraData.putFloat(track_duration_in_millis,
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