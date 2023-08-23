package com.example.musicapp.app.core

import android.util.Log
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.ToMediaItemMapper.Base.Companion.big_img_url
import com.example.musicapp.app.core.ToMediaItemMapper.Base.Companion.is_cached
import com.example.musicapp.app.core.ToMediaItemMapper.Base.Companion.owner_id
import com.example.musicapp.app.core.ToMediaItemMapper.Base.Companion.small_img_url
import com.example.musicapp.app.core.ToMediaItemMapper.Base.Companion.track_duration_in_millis
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
                smallImgUrl = data.mediaMetadata.extras?.getString(small_img_url)!!,
                bigImgUrl = data.mediaMetadata.extras?.getString(big_img_url)!!,
                name = data.mediaMetadata.title.toString(),
                artistName = data.mediaMetadata.artist.toString(),
                albumName = data.mediaMetadata.albumTitle.toString(),
                date = 0,
                durationInSeconds = TimeUnit.MILLISECONDS.toSeconds(
                    data.mediaMetadata.extras?.getFloat(track_duration_in_millis)!!.toLong()
                ).toInt(),
                ownerId = data.mediaMetadata.extras?.getInt(owner_id)!!,
                isCached = data.mediaMetadata.extras?.getBoolean(is_cached)!!,
            )
        }
    }
}