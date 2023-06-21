package com.example.musicapp.app.core

import android.net.Uri
import android.support.v4.os.IResultReceiver2.Default
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import com.example.musicapp.favorites.data.cache.TrackCache
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

/**
 * Created by HP on 21.03.2023.
 **/
class ToTrackCacheMapper @Inject constructor(): Mapper<MediaItem, TrackCache> {

    override  fun map(data: MediaItem): TrackCache {
        return TrackCache(
            id =  data.mediaId.toInt(),
            name = data.mediaMetadata.title.toString(),
            artistName =  data.mediaMetadata.artist.toString(),
            albumName = data.mediaMetadata.albumTitle.toString(),
            date = Calendar.getInstance().timeInMillis.toInt(),
            url = "", bigImgUrl = "", smallImgUrl = "",

        )
    }
}

class ToMediaItemMapper @Inject constructor(): Mapper<TrackCache, MediaItem> {

    override  fun map(data: TrackCache): MediaItem {
        return MediaItem.Builder()
            .setMediaId(if(data.url.isNotEmpty()) data.url else java.util.Random().nextInt().toString())
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(data.name)
                    .setArtist(data.artistName)
                    .setAlbumTitle(data.albumName)
                    .setArtworkUri(Uri.parse(data.smallImgUrl))
                    .setDescription(data.bigImgUrl)
                    .build()
            )
            .build()
    }
}