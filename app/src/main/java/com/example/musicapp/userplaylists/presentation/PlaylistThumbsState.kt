package com.example.musicapp.userplaylists.presentation

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.musicapp.app.core.ImageLoader

/**
 * Created by HP on 13.07.2023.
 **/
sealed interface PlaylistThumbsState{

    fun apply(
        imageView: ImageView,
        imageLoader: ImageLoader,
        cacheStrategy: DiskCacheStrategy
    )

    fun map(): String

    object Empty: PlaylistThumbsState {

        override fun apply(imageView: ImageView, imageLoader: ImageLoader,cacheStrategy: DiskCacheStrategy) {
            imageView.visibility = View.GONE
        }

       override fun map(): String = ""
    }

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