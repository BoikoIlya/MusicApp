package com.example.musicapp.app.core

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.big_img_url
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.date
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.content_id
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.owner_id
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.track_id
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.small_img_url
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.track_duration_in_millis
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.track_duration_formatted
import com.example.musicapp.favorites.data.cache.TrackCache
import java.time.Instant
import javax.inject.Inject

/**
 * Created by HP on 21.03.2023.
 **/

class ToMediaItemMapper @Inject constructor(): Mapper<TrackCache, MediaItem> {

    companion object{
        const val track_duration_in_millis = "duration"
        const val track_duration_formatted = "duration_formatted"
        const val big_img_url = "big_img_url"
        const val small_img_url = "small_img_url"
        const val track_id = "track_id"
        const val date = "track_date"
        const val content_id = "content_id"
        const val owner_id = "owner_id"
    }
    override  fun map(data: TrackCache): MediaItem {

        val extraData = Bundle()
        extraData.putString(track_duration_formatted,data.durationFormatted)
        extraData.putString(big_img_url,data.bigImgUrl)
        extraData.putString(small_img_url,data.smallImgUrl)
        extraData.putInt(track_id,data.trackId)
        extraData.putInt(date,data.date)
        extraData.putInt(owner_id,data.ownerId)
        extraData.putFloat(track_duration_in_millis, data.durationInMillis)


        return MediaItem.Builder()
            .setMediaId(if(data.url.isNotEmpty()) data.url else java.util.Random().nextInt().toString())
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(data.name)
                    .setArtist(data.artistName)
                    .setAlbumTitle(data.albumName)
                    .setArtworkUri(Uri.parse(data.smallImgUrl))
                    .setExtras(extraData)
                    .setIsPlayable(data.url.isNotEmpty())
                    .build()
            )
            .build()
    }
}