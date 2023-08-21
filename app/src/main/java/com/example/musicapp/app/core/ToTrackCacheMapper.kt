package com.example.musicapp.app.core

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.datasource.cache.SimpleCache
import com.bumptech.glide.Glide
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.big_img_url
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.date
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.owner_id
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.track_id
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.small_img_url
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.track_duration_in_millis
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.track_duration_formatted
import com.example.musicapp.favorites.data.cache.TrackCache
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import kotlin.time.measureTime

/**
 * Created by HP on 21.03.2023.
 **/

class ToMediaItemMapper @Inject constructor(
    private val cache: SimpleCache,
   // private val imageSignatureExtractor: ImageSignatureExtractor
): Mapper<TrackCache, MediaItem> {

    companion object{
        const val track_duration_in_millis = "duration"
        const val track_duration_formatted = "duration_formatted"
        const val big_img_url = "big_img_url"
        const val small_img_url = "small_img_url"
        const val track_id = "track_id"
        const val date = "track_date"
        const val owner_id = "owner_id"
        const val is_cached = "is_cached"
        //const val small_img_signature = "is_cached"
    }
    override  fun map(data: TrackCache): MediaItem {

        val extraData = Bundle()
        extraData.putString(track_duration_formatted,data.durationFormatted)
        extraData.putString(big_img_url,data.bigImgUrl)
        extraData.putString(small_img_url,data.smallImgUrl)
        extraData.putString(track_id,data.trackId)
        extraData.putInt(date,data.date)
        extraData.putInt(owner_id,data.ownerId)
        extraData.putFloat(track_duration_in_millis, data.durationInMillis)
      // extraData.putString(small_img_signature,imageSignatureExtractor.extract(small_img_url))


        val length = cache.getCachedLength(Uri.parse(data.url).toString(),0,Long.MAX_VALUE)
        val isCached = cache.isCached(Uri.parse(data.url).toString(),0,length)

//        val length = cache.getCachedLength(data.trackId.toString(),0,Long.MAX_VALUE)
//        val isCached = cache.isCached(data.trackId.toString(),0,length)
//
//        Log.d("tag", "map: $isCached $length ${data.name} ")
        extraData.putBoolean(is_cached,false)

        return MediaItem.Builder()
            .setMediaId(data.trackId)
            .setUri(Uri.parse(if(data.url.isNotEmpty()) data.url else ""))
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
            .setTag(UUID.randomUUID()) //random string to make hashcode different
            .build()
    }
}