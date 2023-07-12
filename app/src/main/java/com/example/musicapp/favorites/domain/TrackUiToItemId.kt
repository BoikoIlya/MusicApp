package com.example.musicapp.favorites.domain


import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.Mapper
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.track_id
import javax.inject.Inject

/**
 * Created by HP on 09.07.2023.
 **/
interface TrackUiToItemId: Mapper<MediaItem,Int> {

    class Base @Inject constructor(): TrackUiToItemId{

        override fun map(data: MediaItem): Int = data.mediaMetadata.extras?.getInt(track_id)!!

    }
}