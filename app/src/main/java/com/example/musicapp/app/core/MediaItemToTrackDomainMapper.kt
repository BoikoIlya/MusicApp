package com.example.musicapp.app.core

import android.util.Log
import androidx.media3.common.MediaItem
import com.example.musicapp.trending.domain.TrackDomain
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by HP on 04.07.2023.
 **/
interface MediaItemToTrackDomainMapper: Mapper<MediaItem, TrackDomain> {

    class Base @Inject constructor(): MediaItemToTrackDomainMapper {

        override fun map(data: MediaItem): TrackDomain {
            return TrackDomain(
                id = data.mediaId.toInt(),
                track_url = data.localConfiguration!!.uri.toString(),
                smallImgUrl = data.mediaMetadata.extras?.getString(ToMediaItemMapper.small_img_url)!!,
                bigImgUrl = data.mediaMetadata.extras?.getString(ToMediaItemMapper.big_img_url)!!,
                name = data.mediaMetadata.title.toString(),
                artistName = data.mediaMetadata.artist.toString(),
                albumName = data.mediaMetadata.albumTitle.toString(),
                date = 0,
                durationInSeconds = TimeUnit.MILLISECONDS.toSeconds(
                    data.mediaMetadata.extras?.getFloat(ToMediaItemMapper.track_duration_in_millis)!!.toLong()
                ).toInt(),
                ownerId = data.mediaMetadata.extras?.getInt(ToMediaItemMapper.owner_id)!!,
                isCached = data.mediaMetadata.extras?.getBoolean(ToMediaItemMapper.is_cached)!!,
            )
        }
    }
}