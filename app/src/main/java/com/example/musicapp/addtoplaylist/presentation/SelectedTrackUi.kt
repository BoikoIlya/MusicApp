package com.example.musicapp.addtoplaylist.presentation

import android.content.Context
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.musicapp.R
import com.example.musicapp.addtoplaylist.domain.SelectedTrackDomain
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.databinding.TrackItemBinding
import javax.inject.Inject

/**
 * Created by HP on 26.07.2023.
 **/
data class SelectedTrackUi(
    private val id: Int,
    private val title: String,
    private val author: String,
    private val durationFormatted: String,
    private val smallImageUrl: String,
    private val selectedIconVisibility: Int,
    private val backgroundColor: Int
){

    fun <T>map(mapper: Mapper<T>): T = mapper.map(
        id,
        title,
        author,
        durationFormatted,
        smallImageUrl,
        selectedIconVisibility,
        backgroundColor
    )

    interface Mapper<T>{
        fun map(
            id: Int,
            title: String,
            author: String,
            durationFormatted: String,
            smallImageUrl: String,
            selectedIconVisibility: Int,
            backgroundColor: Int
        ):T
    }

    class ShowToUi(
       private val binding: TrackItemBinding,
       private val imageLoader: ImageLoader,
       private val context: Context
    ): Mapper<Unit>{
        override fun map(
            id: Int,
            title: String,
            author: String,
            durationFormatted: String,
            smallImageUrl: String,
            selectedIconVisibility: Int,
            backgroundColor: Int,
        ) {
            with(binding) {
                imageLoader.loadImage(smallImageUrl, trackImg,cacheStrategy = DiskCacheStrategy.AUTOMATIC)
                songNameTv.text = title
                authorTv.text = context.getString(R.string.divider_dot) + author
                trackDurationTv.text = durationFormatted
                root.setBackgroundColor(backgroundColor)
                addBtn.apply {
                    setImageResource(R.drawable.check)
                    imageTintList = ContextCompat.getColorStateList(context, R.color.black)
                    visibility = selectedIconVisibility
                    background = null
                    isClickable = false
                }
            }
        }

    }


    class ToDomain @Inject constructor(): Mapper<SelectedTrackDomain> {
        override fun map(
            id: Int,
            title: String,
            author: String,
            durationFormatted: String,
            smallImageUrl: String,
            selectedIconVisibility: Int,
            backgroundColor: Int,
        ): SelectedTrackDomain = SelectedTrackDomain(id, title, author, durationFormatted, smallImageUrl)
    }

    fun map(item: SelectedTrackUi) = item.id == id


}
