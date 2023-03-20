package com.example.musicapp.main.presentation

import android.view.View
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.databinding.ActivityPlayerBinding

/**
 * Created by HP on 31.01.2023.
 **/
interface PlayerControlsState {

    fun apply(
        binding: ActivityMainBinding,
        imageLoader: ImageLoader
    )

    fun apply(binding: ActivityPlayerBinding)

    abstract class Active(
        private val track: MediaItem
    ): PlayerControlsState {

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
            playBtn.isChecked = true
        }

        override fun apply(binding: ActivityPlayerBinding) = with(binding) {
            playSongBtn.isChecked = true
        }
    }

    data class Play(
        private val track: MediaItem
            ) : Active(track){
        override fun apply( binding: ActivityMainBinding,
                            imageLoader: ImageLoader) = with(binding) {

            super.apply(this, imageLoader)
            playBtn.isChecked = false
        }

        override fun apply(binding: ActivityPlayerBinding) = with(binding) {
            playSongBtn.isChecked = false
        }
    }

    object Resume : Active(MediaItem.Builder().build()){
        override fun apply( binding: ActivityMainBinding,
                            imageLoader: ImageLoader) = with(binding) {
            super.apply(this, imageLoader)
            playBtn.isChecked = false
        }

        override fun apply(binding: ActivityPlayerBinding) = with(binding) {
            playSongBtn.isChecked = false
        }
    }



    object Disabled: PlayerControlsState {

        override fun apply(
            binding: ActivityMainBinding,
            imageLoader: ImageLoader
        ) {
           binding.bottomPlayerBar.visibility = View.GONE
        }

        override fun apply(binding: ActivityPlayerBinding) = with(binding) {
            playSongBtn.isChecked = true
        }

    }


}