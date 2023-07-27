package com.example.musicapp.addtoplaylist.domain

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.musicapp.addtoplaylist.presentation.SelectedTrackUi
import com.example.musicapp.app.core.ToMediaItemMapper
import javax.inject.Inject

/**
 * Created by HP on 26.07.2023.
 **/
data class SelectedTrackDomain(
    private val id: Int,
    private val title: String,
    private val author: String,
    private val durationFormatted: String,
    private val smallImageUrl: String,
){

    fun <T>map(mapper: Mapper<T>): T = mapper.map(
        id,
        title,
        author,
        durationFormatted,
        smallImageUrl,
    )

    interface Mapper<T>{
        fun map(
            id: Int,
            title: String,
            author: String,
            durationFormatted: String,
            smallImageUrl: String,
        ):T
    }




    class ToUiSelected(
       private val selectedIconVisibility: Int,
       private val backgroundColor: Int
    ) : Mapper<SelectedTrackUi> {
        override fun map(
            id: Int,
            title: String,
            author: String,
            durationFormatted: String,
            smallImageUrl: String,
        ): SelectedTrackUi {
            return SelectedTrackUi(
                id = id,
                title = title,
                author = author,
                durationFormatted = durationFormatted,
                smallImageUrl = smallImageUrl,
                selectedIconVisibility = selectedIconVisibility,
                backgroundColor = backgroundColor
            )
        }
    }

    class ToIdMapper @Inject constructor(): Mapper<Int> {
        override fun map(
            id: Int,
            title: String,
            author: String,
            durationFormatted: String,
            smallImageUrl: String,
        ): Int = id
    }

    fun map(item: SelectedTrackDomain) = item.id == id
}
