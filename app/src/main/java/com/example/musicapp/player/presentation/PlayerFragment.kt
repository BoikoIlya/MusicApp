package com.example.musicapp.player.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicapp.R
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.databinding.PlayerFragmentBinding
import com.example.musicapp.main.di.App
import com.example.musicapp.main.presentation.MainViewModel
import com.example.musicapp.main.presentation.PlayerCommunicationState
import com.example.musicapp.player.di.PlayerComponent
import com.example.musicapp.trending.presentation.TrendingViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.slider.Slider
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi /**
 * Created by HP on 22.04.2023.
 **/
class PlayerFragment: Fragment(R.layout.player_fragment) {

    private val binding by viewBinding(PlayerFragmentBinding::bind)

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var viewModel: PlayerViewModel

    private lateinit var currentTrack: MediaItem

    private lateinit var playerComponent: PlayerComponent

    override fun onAttach(context: Context) {
        super.onAttach(context)
        playerComponent = (context.applicationContext as App).appComponent.playerComponent().build()
        playerComponent.inject(this)
        viewModel = ViewModelProvider(this, factory)[PlayerViewModel::class.java]
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.collectIsSaved(this@PlayerFragment) {
                binding.likeBtn.isChecked = it
            }
        }

        lifecycleScope.launch {
            viewModel.collectSelectedTrack(this@PlayerFragment) {
                viewModel.isSaved(it.mediaId)
                with(binding) {
                    with(it.mediaMetadata) {
                        albumName.text = albumTitle
                        imageLoader.loadImage(
                            description.toString(),// description contain big img url
                            songImg,
                            imgBg
                        )
                        songName.text = title
                        songAuthor.text = artist
                    }
                    currentTrack = it
                }
            }
        }

        lifecycleScope.launch {
            viewModel.collectPlayerControls(this@PlayerFragment) {
                it.apply(binding, viewModel)
            }
        }

        binding.shuffleSongBtn.isChecked = viewModel.isShuffleModeEnabled()
        binding.repeatSongBtn.isChecked = viewModel.isRepeatEnabled()


        lifecycleScope.launch {
            viewModel.collectTrackPosition(this@PlayerFragment) {
                binding.slider.value = it.first.toFloat()
                binding.currentPosition.text = it.second
            }
        }

        lifecycleScope.launch {
            viewModel.collectTrackDurationCommunication(this@PlayerFragment) {
                binding.totalDuration.text = viewModel.durationForTextView(it)
                binding.slider.valueTo = it.toFloat()
            }
        }

        binding.slider.addOnChangeListener(Slider.OnChangeListener { _, value, user ->
           if(user) viewModel.playerAction(PlayerCommunicationState.SeekToPosition(value.toLong()))
        })


        binding.backBtn.setOnClickListener {
            viewModel.bottomSheetState(BottomSheetBehavior.STATE_COLLAPSED)
        }

        binding.playSongBtn.setOnClickListener {
            if ((it as ToggleButton).isChecked)
                viewModel.playerAction(PlayerCommunicationState.Pause)
            else
                viewModel.playerAction(PlayerCommunicationState.Resume)
        }

        binding.previousSongBtn.setOnClickListener {
            viewModel.playerAction(PlayerCommunicationState.Previous)
        }

        binding.nextSongBtn.setOnClickListener {
            viewModel.playerAction(PlayerCommunicationState.Next)
        }

        binding.shuffleSongBtn.setOnClickListener {
            if ((it as ToggleButton).isChecked)
                viewModel.playerAction(PlayerCommunicationState.ShuffleMode(PlayerCommunicationState.ShuffleMode.ENABLE_SHUFFLE))
            else
                viewModel.playerAction(PlayerCommunicationState.ShuffleMode(PlayerCommunicationState.ShuffleMode.DISABLE_SHUFFLE))
        }

        binding.repeatSongBtn.setOnClickListener {
            if ((it as ToggleButton).isChecked)
                viewModel.playerAction(PlayerCommunicationState.RepeatMode(Player.REPEAT_MODE_ONE))
            else
                viewModel.playerAction(PlayerCommunicationState.RepeatMode(Player.REPEAT_MODE_OFF))


        }

        binding.likeBtn.setOnClickListener {
            if ((it as ToggleButton).isChecked)
                viewModel.saveTrack(currentTrack)
            else
                viewModel.removeTrack(currentTrack.mediaId)
        }

        binding.moveToQueue.setOnClickListener {
            viewModel.slidePage(1)
        }
    }
}