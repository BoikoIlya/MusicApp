package com.example.musicapp.main.presentation

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.app.core.ToMediaItemMapper
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.databinding.PlayerFragmentBinding
import com.example.musicapp.player.presentation.PlayerViewModel


/**
 * Created by HP on 31.01.2023.
 **/
interface PlayerControlsState {

    fun apply(
        binding: ActivityMainBinding,
        imageLoader: ImageLoader,
        viewModel: MainViewModel,
    )

    fun apply(
        binding: PlayerFragmentBinding,
        viewModel: PlayerViewModel
    )


    @UnstableApi
    abstract class Active(
        private val track: MediaItem,
    ): PlayerControlsState {

        override fun apply(
            binding: ActivityMainBinding,
            imageLoader: ImageLoader,
            viewModel: MainViewModel,
        ) {
            with(binding) {

                imageLoader.loadImage(
                    track.mediaMetadata.extras?.getString(ToMediaItemMapper.small_img_url) ?: "",
                    trackImg
                )
                songNameTv.text = track.mediaMetadata.title
                songAuthorName.text = track.mediaMetadata.artist

                if (bottomPlayerBar.visibility != View.VISIBLE) {
                    bottomPlayerBar.visibility = View.VISIBLE
                    bottomPlayerBar.startAnimation(
                        AnimationUtils.loadAnimation(
                            bottomPlayerBar.context,
                            R.anim.bottom_bar
                        )
                    )
                }

            }


        }

    }


        @UnstableApi
        data class Pause(
            private val track: MediaItem,
        ) : Active(track) {

            override fun apply(
                binding: ActivityMainBinding,
                imageLoader: ImageLoader,
                viewModel: MainViewModel,
            ) = with(binding) {
                super.apply(this, imageLoader, viewModel)
                playBtn.isChecked = true
            }

            override fun apply(binding: PlayerFragmentBinding, viewModel: PlayerViewModel) {
                binding.playSongBtn.isChecked = true
            }
        }

        @UnstableApi
        data class Play(
            private val track: MediaItem,
        ) : Active(track) {
            override fun apply(
                binding: ActivityMainBinding,
                imageLoader: ImageLoader,
                viewModel: MainViewModel,
            ) = with(binding) {
                super.apply(this, imageLoader, viewModel)
                playBtn.isChecked = false

            }

            override fun apply(binding: PlayerFragmentBinding, viewModel: PlayerViewModel) {
                binding.playSongBtn.isChecked = false
            }
        }


        object Disabled : PlayerControlsState {

            override fun apply(
                binding: ActivityMainBinding,
                imageLoader: ImageLoader,
                viewModel: MainViewModel,
            ) {
                binding.bottomPlayerBar.visibility = View.GONE
            }

            override fun apply(binding: PlayerFragmentBinding, viewModel: PlayerViewModel) {
                binding.playSongBtn.isChecked = true
            }



        }

}