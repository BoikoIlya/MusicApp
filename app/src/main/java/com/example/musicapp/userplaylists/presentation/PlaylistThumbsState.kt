package com.example.musicapp.userplaylists.presentation

import android.view.View
import android.widget.ImageView
import com.example.musicapp.app.core.ImageLoader

/**
 * Created by HP on 13.07.2023.
 **/
sealed interface PlaylistThumbsState{

    fun apply(
        imageView: ImageView,
        imageLoader: ImageLoader
    )

    fun map(): String

    object Empty: PlaylistThumbsState {

        override fun apply(imageView: ImageView, imageLoader: ImageLoader) {
            imageView.visibility = View.GONE
        }

       override fun map(): String = ""
    }

    data class LoadImages(
        private val url: String
    ): PlaylistThumbsState {

        override fun apply(imageView: ImageView, imageLoader: ImageLoader) {
            imageLoader.loadImageForPlaylist(url,imageView)
            imageView.visibility = View.VISIBLE
        }

      override fun map(): String = url
    }
}