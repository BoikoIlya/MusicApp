package com.kamancho.melisma.main.presentation

import android.view.View
import android.view.animation.AnimationUtils
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.app.core.PlayerAction
import com.kamancho.melisma.app.core.ToMediaItemMapper.Base.Companion.small_img_url
import com.kamancho.melisma.databinding.ActivityMainBinding
import com.kamancho.melisma.databinding.ArtistsTracksDialogFragmentBinding
import com.kamancho.melisma.databinding.BottomPlayerBinding
import com.kamancho.melisma.databinding.PlayerFragmentBinding
import com.kamancho.melisma.player.presentation.PlayerViewModel


/**
 * Created by HP on 31.01.2023.
 **/
interface PlayerControlsState {

    fun apply(
        binding: BottomPlayerBinding,
        imageLoader: ImageLoader,
        viewModel: PlayerAction,
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
            binding: BottomPlayerBinding,
            imageLoader: ImageLoader,
            viewModel: PlayerAction,
        ) {
            with(binding) {

                imageLoader.loadImage(
                    track.mediaMetadata.extras?.getString(small_img_url) ?: "",
                    trackImg, cacheStrategy = DiskCacheStrategy.NONE,
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
                binding: BottomPlayerBinding,
                imageLoader: ImageLoader,
                viewModel: PlayerAction,
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
                binding: BottomPlayerBinding,
                imageLoader: ImageLoader,
                viewModel: PlayerAction,
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
                binding: BottomPlayerBinding,
                imageLoader: ImageLoader,
                viewModel: PlayerAction,
            ) {
                binding.bottomPlayerBar.visibility = View.GONE
            }

            override fun apply(binding: PlayerFragmentBinding, viewModel: PlayerViewModel) {
                binding.playSongBtn.isChecked = true
            }



        }

}