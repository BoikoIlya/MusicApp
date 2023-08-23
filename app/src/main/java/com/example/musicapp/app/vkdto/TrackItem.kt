package com.example.musicapp.app.vkdto



import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.musicapp.app.core.ToMediaItemMapper
import com.example.musicapp.app.core.ToMediaItemMapper.Base.Companion.big_img_url
import com.example.musicapp.app.core.ToMediaItemMapper.Base.Companion.small_img_url
import com.example.musicapp.app.core.ToMediaItemMapper.Base.Companion.track_duration_formatted
import com.example.musicapp.app.core.ToMediaItemMapper.Base.Companion.track_duration_in_millis
import com.example.musicapp.app.core.ToMediaItemMapper.Base.Companion.track_id
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.trending.domain.TrackDomain
import java.util.concurrent.TimeUnit

import javax.inject.Inject

data class TrackItem(
   private val access_key: String,
   private val ads: Ads,
   private val album: Album?,
   private val artist: String,
   private val content_restricted: Int,
   private val date: Int,
   private val duration: Int,
   private val featured_artists: List<FeaturedArtist>?,
   private val genre_id: Int,
   private val id: Int,
   private val is_explicit: Boolean,
   private val is_focus_track: Boolean,
   private val is_licensed: Boolean,
   private val lyrics_id: Int,
   private val main_artists: List<MainArtist>?,
   private val no_search: Int,
   private val owner_id: Int,
   private val short_videos_allowed: Boolean,
   private val stories_allowed: Boolean,
   private val stories_cover_allowed: Boolean,
   private val subtitle: String?,
   private val title: String,
   private val track_code: String?,
   private val url: String
){

    fun <T>map(mapper: Mapper<T>):T = mapper.map(
        access_key,
        ads,
        album,
        artist,
        content_restricted,
        date,
        duration,
        featured_artists,
        genre_id,
        id,
        is_explicit,
        is_focus_track,
        is_licensed,
        lyrics_id,
        main_artists,
        no_search,
        owner_id,
        short_videos_allowed,
        stories_allowed,
        stories_cover_allowed,
        subtitle,
        title,
        track_code?:"",
        url
    )

    interface Mapper<T> {

        fun map(
            access_key: String,
            ads: Ads,
            album: Album?,
            artist: String,
            content_restricted: Int,
            date: Int,
            duration: Int,
            featured_artists: List<FeaturedArtist>?,
            genre_id: Int,
            id: Int,
            is_explicit: Boolean,
            is_focus_track: Boolean,
            is_licensed: Boolean,
            lyrics_id: Int,
            main_artists: List<MainArtist>?,
            no_search: Int,
            owner_id: Int,
            short_videos_allowed: Boolean,
            stories_allowed: Boolean,
            stories_cover_allowed: Boolean,
            subtitle: String?,
            title: String,
            track_code: String,
            url: String
        ): T

        class CloudTrackToTrackCacheMapper @Inject constructor() : Mapper<TrackCache> {
            override fun map(
                access_key: String,
                ads: Ads,
                album: Album?,
                artist: String,
                content_restricted: Int,
                date: Int,
                duration: Int,
                featured_artists: List<FeaturedArtist>?,
                genre_id: Int,
                id: Int,
                is_explicit: Boolean,
                is_focus_track: Boolean,
                is_licensed: Boolean,
                lyrics_id: Int,
                main_artists: List<MainArtist>?,
                no_search: Int,
                owner_id: Int,
                short_videos_allowed: Boolean,
                stories_allowed: Boolean,
                stories_cover_allowed: Boolean,
                subtitle: String?,
                title: String,
                track_code: String,
                url: String,
            ): TrackCache {
                val minutes: Long = TimeUnit.SECONDS.toMinutes(duration.toLong())
                val seconds: Long = duration - TimeUnit.MINUTES.toSeconds(minutes)
                val durationString = String.format("%02d:%02d", minutes, seconds)


                return TrackCache(
                    trackId = id.toString(),
                    url = url,
                    name = title,
                    artistName = artist,
                    bigImgUrl = album?.thumb?.photo_1200 ?: "",
                    smallImgUrl = album?.thumb?.photo_135 ?: "",
                    albumName = album?.title ?: "",
                    date = date,
                    durationFormatted = durationString,
                    ownerId = owner_id,
                    durationInMillis = TimeUnit.SECONDS.toMillis(duration.toLong()).toFloat(),
                )
            }

        }


        class CloudTrackToTrackDomainMapper @Inject constructor() : Mapper<TrackDomain> {
            override fun map(
                access_key: String,
                ads: Ads,
                album: Album?,
                artist: String,
                content_restricted: Int,
                date: Int,
                duration: Int,
                featured_artists: List<FeaturedArtist>?,
                genre_id: Int,
                id: Int,
                is_explicit: Boolean,
                is_focus_track: Boolean,
                is_licensed: Boolean,
                lyrics_id: Int,
                main_artists: List<MainArtist>?,
                no_search: Int,
                owner_id: Int,
                short_videos_allowed: Boolean,
                stories_allowed: Boolean,
                stories_cover_allowed: Boolean,
                subtitle: String?,
                title: String,
                track_code: String,
                url: String,
            ): TrackDomain {

                return TrackDomain(
                    id = id,
                    name = title,
                    artistName = artist,
                    albumName = album?.title ?: "",
                    date = date,
                    durationInSeconds = duration,
                    smallImgUrl = album?.thumb?.photo_135 ?: "",
                    bigImgUrl = album?.thumb?.photo_1200 ?: "",
                    track_url = url,
                    ownerId = owner_id,
                    isCached = false
                )
            }


        }

        class CloudTrackToMediaItemMapper @Inject constructor() : Mapper<MediaItem> {
            override fun map(
                access_key: String,
                ads: Ads,
                album: Album?,
                artist: String,
                content_restricted: Int,
                date: Int,
                duration: Int,
                featured_artists: List<FeaturedArtist>?,
                genre_id: Int,
                id: Int,
                is_explicit: Boolean,
                is_focus_track: Boolean,
                is_licensed: Boolean,
                lyrics_id: Int,
                main_artists: List<MainArtist>?,
                no_search: Int,
                owner_id: Int,
                short_videos_allowed: Boolean,
                stories_allowed: Boolean,
                stories_cover_allowed: Boolean,
                subtitle: String?,
                title: String,
                track_code: String,
                url: String,
            ): MediaItem {
                val minutes: Long = TimeUnit.SECONDS.toMinutes(duration.toLong())
                val seconds: Long = duration - TimeUnit.MINUTES.toSeconds(minutes)
                val durationString = String.format("%02d:%02d", minutes, seconds)

                val extraData = Bundle()
                extraData.putString(track_duration_formatted, durationString)
                extraData.putString(
                    big_img_url,
                    album?.thumb?.photo_1200 ?: ""
                )
                extraData.putString(
                    small_img_url,
                    album?.thumb?.photo_135 ?: ""
                )
                extraData.putInt(track_id, id)
                extraData.putFloat(track_duration_in_millis, TimeUnit.SECONDS.toMillis(duration.toLong()).toFloat())
                extraData.putInt(ToMediaItemMapper.Base.owner_id,owner_id)

                return MediaItem.Builder()
                    .setMediaId(id.toString())
                    .setUri(if (url.isNotEmpty()) url else "")
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle(title)
                            .setArtist(artist)
                            .setAlbumTitle(album?.title?:"")
                            .setArtworkUri(Uri.parse(album?.thumb?.photo_135 ?: ""))
                            .setExtras(extraData)
                            .setIsPlayable(url.isNotEmpty())
                            .build()
                    )
                    .build()
            }

        }
    }

    class ModifiedIdToTrackCacheMapper(
        private val friendId: String
    ) : Mapper<TrackCache> {
        override fun map(
            access_key: String,
            ads: Ads,
            album: Album?,
            artist: String,
            content_restricted: Int,
            date: Int,
            duration: Int,
            featured_artists: List<FeaturedArtist>?,
            genre_id: Int,
            id: Int,
            is_explicit: Boolean,
            is_focus_track: Boolean,
            is_licensed: Boolean,
            lyrics_id: Int,
            main_artists: List<MainArtist>?,
            no_search: Int,
            owner_id: Int,
            short_videos_allowed: Boolean,
            stories_allowed: Boolean,
            stories_cover_allowed: Boolean,
            subtitle: String?,
            title: String,
            track_code: String,
            url: String,
        ): TrackCache {
            val minutes: Long = TimeUnit.SECONDS.toMinutes(duration.toLong())
            val seconds: Long = duration - TimeUnit.MINUTES.toSeconds(minutes)
            val durationString = String.format("%02d:%02d", minutes, seconds)


            return TrackCache(
                trackId = id.toString()+friendId,
                url = url,
                name = title,
                artistName = artist,
                bigImgUrl = album?.thumb?.photo_1200 ?: "",
                smallImgUrl = album?.thumb?.photo_135 ?: "",
                albumName = album?.title ?: "",
                date = date,
                durationFormatted = durationString,
                ownerId = owner_id,
                durationInMillis = TimeUnit.SECONDS.toMillis(duration.toLong()).toFloat(),
            )
        }

    }
}