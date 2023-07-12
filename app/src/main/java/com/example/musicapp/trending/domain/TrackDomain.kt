package com.example.musicapp.trending.domain

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.musicapp.app.core.FormatTimeSecondsToMinutesAndSeconds
import com.example.musicapp.app.core.ToMediaItemMapper
import com.example.musicapp.favorites.data.cache.TrackCache
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
    private val ownerId: Int

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
            ownerId: Int
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
        ownerId
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
            ownerId: Int
        ): MediaItem {

           val durationString = formatTimeSecondsToMinutesAndSeconds.format(duration)

            val extraData = Bundle()
            extraData.putString(ToMediaItemMapper.track_duration_formatted,durationString)
            extraData.putString(ToMediaItemMapper.big_img_url,bigImgUrl)
            extraData.putString(ToMediaItemMapper.small_img_url,smallImgUrl)
            extraData.putInt(ToMediaItemMapper.track_id,id)
            extraData.putInt(ToMediaItemMapper.owner_id,ownerId)
            extraData.putFloat(ToMediaItemMapper.track_duration_in_millis, TimeUnit.SECONDS.toMillis(duration.toLong()).toFloat())

            return MediaItem.Builder()
                .setMediaId(if(track_url.isNotEmpty()) track_url else java.util.Random().nextInt().toString())
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
        ): TrackCache {
            return TrackCache(
                id = id,
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
            ownerId: Int
        ): Pair<String,String> {
            return Pair(name,artistName)
        }


    }

}