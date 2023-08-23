package com.kamancho.melisma.trending.domain

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.kamancho.melisma.app.core.FormatTimeSecondsToMinutesAndSeconds
import com.kamancho.melisma.app.core.ToMediaItemMapper.Base.Companion.big_img_url
import com.kamancho.melisma.app.core.ToMediaItemMapper.Base.Companion.is_cached
import com.kamancho.melisma.app.core.ToMediaItemMapper.Base.Companion.owner_id
import com.kamancho.melisma.app.core.ToMediaItemMapper.Base.Companion.small_img_url
import com.kamancho.melisma.app.core.ToMediaItemMapper.Base.Companion.track_duration_formatted
import com.kamancho.melisma.app.core.ToMediaItemMapper.Base.Companion.track_duration_in_millis
import com.kamancho.melisma.app.core.ToMediaItemMapper.Base.Companion.track_id
import com.kamancho.melisma.favorites.data.cache.TrackCache
import java.time.Instant
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by HP on 27.01.2023.
 **/

data class TrackDomain(
    private val id: Int,
    private val track_url: String,
    private val smallImgUrl: String,
    private val bigImgUrl: String,
    private val name: String,
    private val artistName: String,
    private val albumName: String,
    private val date: Int,
    private val durationInSeconds: Int,
    private val ownerId: Int,
    private val isCached: Boolean
){
    interface Mapper<T>{
        fun map(
            id: Int,
            track_url: String,
            smallImgUrl: String,
            bigImgUrl: String,
            name: String,
            artistName: String,
            albumName: String,
            date: Int,
            duration: Int,
            ownerId: Int,
            isCached: Boolean
        ): T
    }

    fun <T>map(mapper: Mapper<T>): T  = mapper.map(
        id,
        track_url,
        smallImgUrl,
        bigImgUrl,
        name,
        artistName,
        albumName,
        date,
        durationInSeconds,
        ownerId,
        isCached
    )

    class ToTrackUiMapper @Inject constructor(
        private val formatTimeSecondsToMinutesAndSeconds: FormatTimeSecondsToMinutesAndSeconds
    ): Mapper<MediaItem>{

        override fun map(
            id: Int,
            track_url: String,
            smallImgUrl: String,
            bigImgUrl: String,
            name: String,
            artistName: String,
            albumName: String,
            date: Int,
            duration: Int,
            ownerId: Int,
            isCached: Boolean
        ): MediaItem {

           val durationString = formatTimeSecondsToMinutesAndSeconds.format(duration)

            val extraData = Bundle()
            extraData.putString(track_duration_formatted,durationString)
            extraData.putString(big_img_url,bigImgUrl)
            extraData.putString(small_img_url,smallImgUrl)
            extraData.putInt(track_id,id)
            extraData.putInt(owner_id,ownerId)
            extraData.putFloat(track_duration_in_millis, TimeUnit.SECONDS.toMillis(duration.toLong()).toFloat())
            extraData.putBoolean(is_cached,isCached)

            return MediaItem.Builder()
                .setMediaId(id.toString())
                .setUri(track_url)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(name)
                        .setArtist(artistName)
                        .setAlbumTitle(albumName)
                        .setArtworkUri(Uri.parse(smallImgUrl))
                        .setDescription(bigImgUrl)
                        .setExtras(extraData)
                        .setIsPlayable(track_url.isNotEmpty())
                        .build()
                )
                .build()
        }

    }

    class ToTrackCacheMapper @Inject constructor(
        private val formatTimeSecondsToMinutesAndSeconds: FormatTimeSecondsToMinutesAndSeconds
    ): Mapper<TrackCache>{
        override fun map(
            id: Int,
            track_url: String,
            smallImgUrl: String,
            bigImgUrl: String,
            name: String,
            artistName: String,
            albumName: String,
            date: Int,
            duration: Int,
            ownerId: Int,
            isCached: Boolean
        ): TrackCache {
            return TrackCache(
                trackId = id.toString(),
                url = track_url,
                name = name,
                artistName = artistName,
                bigImgUrl = bigImgUrl,
                smallImgUrl = smallImgUrl,
                albumName = albumName,
                date = Instant.now().epochSecond.toInt(),
                durationFormatted = formatTimeSecondsToMinutesAndSeconds.format(duration),
                durationInMillis = TimeUnit.SECONDS.toMillis(duration.toLong()).toFloat(),
                ownerId = ownerId
            )
        }

    }

    class AddToFavoritesCloudMapper @Inject constructor(): Mapper<Pair<Int, Int>>{
        override fun map(
            id: Int,
            track_url: String,
            smallImgUrl: String,
            bigImgUrl: String,
            name: String,
            artistName: String,
            albumName: String,
            date: Int,
            duration: Int,
            ownerId: Int,
            isCached: Boolean
        ): Pair<Int, Int>  = Pair(ownerId,id)



    }

    class DeleteTrackMapper @Inject constructor(): Mapper<Int>{
        override fun map(
            id: Int,
            track_url: String,
            smallImgUrl: String,
            bigImgUrl: String,
            name: String,
            artistName: String,
            albumName: String,
            date: Int,
            duration: Int,
            ownerId: Int,
            isCached: Boolean
        ): Int = id


    }

    class ContainsTrackMapper @Inject constructor(): Mapper<Pair<String,String>>{
        override fun map(
            id: Int,
            track_url: String,
            smallImgUrl: String,
            bigImgUrl: String,
            name: String,
            artistName: String,
            albumName: String,
            date: Int,
            duration: Int,
            ownerId: Int,
            isCached: Boolean
        ): Pair<String,String> {
            return Pair(name,artistName)
        }


    }

}