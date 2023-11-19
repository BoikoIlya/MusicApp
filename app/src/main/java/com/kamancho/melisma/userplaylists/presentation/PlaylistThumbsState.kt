package com.kamancho.melisma.userplaylists.presentation

import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import androidx.annotation.Keep
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kamancho.melisma.app.core.ImageLoader
import kotlinx.parcelize.Parcelize

/**
 * Created by HP on 13.07.2023.
 **/
@Keep
@Parcelize
sealed interface PlaylistThumbsState: Parcelable{

    fun apply(
        imageView: ImageView,
        imageLoader: ImageLoader,
        cacheStrategy: DiskCacheStrategy
    )

    fun map(): String
    @Keep
    @Parcelize
    object Empty: PlaylistThumbsState {

        override fun apply(imageView: ImageView, imageLoader: ImageLoader,cacheStrategy: DiskCacheStrategy) {
            imageView.visibility = View.GONE
        }

       override fun map(): String = ""
    }
    @Keep
    @Parcelize
    data class LoadImages(
        private val url: String
    ): PlaylistThumbsState {

        override fun apply(imageView: ImageView, imageLoader: ImageLoader,cacheStrategy: DiskCacheStrategy) {
            imageLoader.loadImageForPlaylist(url,imageView,cacheStrategy)
            imageView.visibility = View.VISIBLE
        }

      override fun map(): String = url
    }
}