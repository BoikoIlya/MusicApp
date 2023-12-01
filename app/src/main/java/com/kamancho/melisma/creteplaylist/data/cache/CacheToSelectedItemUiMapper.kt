package com.kamancho.melisma.creteplaylist.data.cache

import com.kamancho.melisma.addtoplaylist.presentation.SelectedTrackUi
import com.kamancho.melisma.app.core.Mapper
import com.kamancho.melisma.favorites.data.cache.TrackCache
import javax.inject.Inject

/**
 * Created by HP on 26.07.2023.
 **/
interface CacheToSelectedItemUiMapper: Mapper<TrackCache, SelectedTrackUi> {

    class Base @Inject constructor(): CacheToSelectedItemUiMapper {

        override fun map(data: TrackCache): SelectedTrackUi {
            return SelectedTrackUi(
                id = data.trackId.toInt(),
                title = data.name,
                author = data.artistName,
                durationFormatted = data.durationFormatted,
                smallImageUrl = data.smallImgUrl,
                selectedIconVisibility = 0,
                backgroundColor = 0
            )
        }
    }
}