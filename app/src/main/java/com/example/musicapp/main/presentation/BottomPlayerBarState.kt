package com.example.musicapp.main.presentation

import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.ToggleButton
import android.widget.VideoView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.databinding.ActivityMainBinding

/**
 * Created by HP on 31.01.2023.
 **/
interface BottomPlayerBarState {

    fun apply(
        binding: ActivityMainBinding,
        imageLoader: ImageLoader
    )

    abstract class Active(
        private val track: MediaItem
    ): BottomPlayerBarState {

        override fun apply(
            binding: ActivityMainBinding,
            imageLoader: ImageLoader
        ) {
            with(binding) {
                imageLoader.loadImage(
                    "https://" +
                            track.mediaMetadata.artworkUri?.host +
                            track.mediaMetadata.artworkUri?.path,
                    trackImg
                )
                songNameTv.text = track.mediaMetadata.title
                songAuthorName.text = track.mediaMetadata.artist
                bottomPlayerBar.visibility = View.VISIBLE

            }
            Log.d("tag", "active: ")
        }

    }

    data class  Pause(
        private val track: MediaItem
        ): Active(track){

        override fun apply(
            binding: ActivityMainBinding,
            imageLoader: ImageLoader
        )  = with(binding)  {
            if(bottomPlayerBar.visibility ==View.GONE) super.apply(this, imageLoader)
            bottomPlayBtn.isChecked = true
        }
    }

    data class Play(
        private val track: MediaItem
            ) : Active(track){
        override fun apply( binding: ActivityMainBinding,
                            imageLoader: ImageLoader) = with(binding) {

            Log.d("tag", "play: ")
            super.apply(this, imageLoader)
            bottomPlayBtn.isChecked = false
        }
    }

    object Resume : Active(MediaItem.Builder().build()){
        override fun apply( binding: ActivityMainBinding,
                            imageLoader: ImageLoader) = with(binding) {
            super.apply(this, imageLoader)
            bottomPlayBtn.isChecked = false
        }
    }



    object Disabled: BottomPlayerBarState {

        override fun apply(
            binding: ActivityMainBinding,
            imageLoader: ImageLoader
        ) {
           binding.bottomPlayerBar.visibility = View.GONE
        }

    }


}