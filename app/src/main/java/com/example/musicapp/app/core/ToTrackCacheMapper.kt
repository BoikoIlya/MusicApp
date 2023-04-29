package com.example.musicapp.app.core

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
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
            id =  data.mediaId,
            name = data.mediaMetadata.title.toString(),
            artistName =  data.mediaMetadata.artist.toString(),
            albumName = data.mediaMetadata.albumTitle.toString(),
            time = Calendar.getInstance().timeInMillis,
            imgUrl = data.mediaMetadata.artworkUri.toString()
        )
    }
}

class ToMediaItemMapper @Inject constructor(): Mapper<TrackCache, MediaItem> {

    override  fun map(data: TrackCache): MediaItem {
        return MediaItem.Builder()
            .setMediaId(data.id)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(data.name)
                    .setArtist(data.artistName)
                    .setAlbumTitle(data.albumName)
                    .setArtworkUri(Uri.parse(data.imgUrl))
                    .build()
            )
            .build()
    }
}