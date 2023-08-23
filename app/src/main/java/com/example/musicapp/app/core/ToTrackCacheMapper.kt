package com.example.musicapp.app.core

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.musicapp.downloader.data.cache.DownloadTracksCacheDataSource.Base.Companion.fileExtension

import com.example.musicapp.favorites.data.cache.TrackCache
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import kotlin.time.measureTime

/**
 * Created by HP on 21.03.2023.
 **/

interface ToMediaItemMapper: Mapper<Pair<TrackCache,List<Pair<String,String>>>, MediaItem> {


    class Base @Inject constructor() : ToMediaItemMapper {

        companion object {
            const val track_duration_in_millis = "duration"
            const val track_duration_formatted = "duration_formatted"
            const val big_img_url = "big_img_url"
            const val small_img_url = "small_img_url"
            const val track_id = "track_id"
            const val date = "track_date"
            const val owner_id = "owner_id"
            const val is_cached = "is_cached"
        }

        override fun map(data: Pair<TrackCache, List<Pair<String, String>>>): MediaItem {

            val extraData = Bundle()
            extraData.putString(track_duration_formatted, data.first.durationFormatted)
            extraData.putString(big_img_url, data.first.bigImgUrl)
            extraData.putString(small_img_url, data.first.smallImgUrl)
            extraData.putString(track_id, data.first.trackId)
            extraData.putInt(date, data.first.date)
            extraData.putInt(owner_id, data.first.ownerId)
            extraData.putFloat(track_duration_in_millis, data.first.durationInMillis)

            val downloadedFilepath = data.second.find {
                it.first == String.format("${data.first.name} - ${data.first.artistName}$fileExtension")
            }

            extraData.putBoolean(is_cached, downloadedFilepath != null)


            return MediaItem.Builder()
                .setMediaId(data.first.trackId)
                .setUri(Uri.parse(downloadedFilepath?.second ?: data.first.url))
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(data.first.name)
                        .setArtist(data.first.artistName)
                        .setAlbumTitle(data.first.albumName)
                        .setArtworkUri(Uri.parse(data.first.smallImgUrl))
                        .setExtras(extraData)
                        .setIsPlayable(data.first.url.isNotEmpty())
                        .build()
                )
                .setTag(UUID.randomUUID()) //random string to make hashcode different
                .build()
        }
    }
}