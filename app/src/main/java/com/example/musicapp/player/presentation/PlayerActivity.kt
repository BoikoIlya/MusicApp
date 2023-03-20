package com.example.musicapp.player.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.ToggleButton
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.common.Player.REPEAT_MODE_ONE
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.app.core.Selector
import com.example.musicapp.databinding.ActivityPlayerBinding
import com.example.musicapp.main.di.App
import com.example.musicapp.main.presentation.PlayerCommunicationState
import com.example.musicapp.main.presentation.PlayerCommunicationState.ShuffleMode.Companion.DISABLE_SHUFFLE
import com.example.musicapp.main.presentation.PlayerCommunicationState.ShuffleMode.Companion.ENABLE_SHUFFLE
import com.example.musicapp.trending.presentation.TrendingTracksAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi class PlayerActivity : AppCompatActivity() {

    lateinit var binding: ActivityPlayerBinding

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    lateinit var viewModel: PlayerViewModel

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (this.application as App).appComponent.playerComponent().build().inject(this)
        viewModel = ViewModelProvider(
            this,
            factory
        )[PlayerViewModel::class.java]

        binding.queueRcv.layoutManager = LinearLayoutManager(this)

        val tracksAdapter = TrendingTracksAdapter(
            this,
            playClickListener = object: Selector<MediaItem> {
                override fun onSelect(data: MediaItem, position: Int) {
                    viewModel.playerAction(PlayerCommunicationState.Play(data,position))
                }
            }, saveClickListener = object : ClickListener<MediaItem> {
                override fun onClick(data: MediaItem) {
                    //TODO
                }
            }, imageLoader)

        binding.queueRcv.adapter = tracksAdapter

        lifecycleScope.launch {
            viewModel.collectCurrentQueue(this@PlayerActivity){
                tracksAdapter.map(it)
            }
        }

        lifecycleScope.launch {
            viewModel.collectSelectedTrack(this@PlayerActivity){
                with(binding){
                    albumName.text = it.mediaMetadata.albumTitle
                    imageLoader.loadImage("https://"+ it.mediaMetadata.artworkUri?.host+it.mediaMetadata.artworkUri?.path, songImg)
                    songName.text = it.mediaMetadata.title
                    songAuthor.text = it.mediaMetadata.artist
                    seekBar.max = viewModel.durationSeekBar()
                    totalDuration.text = viewModel.durationForTextView()
                }
                tracksAdapter.newPosition(it)
            }
        }

        binding.shuffleSongBtn.isChecked = viewModel.isShuffleModeEnabled()
        binding.repeatSongBtn.isChecked = viewModel.isRepeatEnabled()

        lifecycleScope.launch{
            viewModel.collectPlayerControls(this@PlayerActivity){
                it.apply(binding)
            }
        }

        lifecycleScope.launch{
            viewModel.collectTrackPosition(this@PlayerActivity){
                binding.seekBar.progress = it.first
                binding.currentPosition.text = it.second
            }
        }

        binding.seekBar.setOnSeekBarChangeListener(object :OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, position: Int, p2: Boolean) {
              if(p2) viewModel.playerAction(PlayerCommunicationState.SeekToPosition(position.toLong()))
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        binding.backBtn.setOnClickListener {
            finish()
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
                viewModel.playerAction(PlayerCommunicationState.ShuffleMode(ENABLE_SHUFFLE))
            else
                viewModel.playerAction(PlayerCommunicationState.ShuffleMode(DISABLE_SHUFFLE))
        }

        binding.repeatSongBtn.setOnClickListener {
            if ((it as ToggleButton).isChecked)
                viewModel.playerAction(PlayerCommunicationState.RepeatMode(REPEAT_MODE_ONE))
            else
                viewModel.playerAction(PlayerCommunicationState.RepeatMode(REPEAT_MODE_OFF))
        }
    }
}