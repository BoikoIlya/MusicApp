package com.example.musicapp.main.presentation

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.musicapp.R
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.main.di.App
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.player.di.PlayerModule
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    lateinit var viewModel: MainViewModel

    val player = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        (this.application as App).appComponent.inject(this)
        viewModel = ViewModelProvider(
            this,
            factory
        )[MainViewModel::class.java]

        setContentView(binding.root)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHost.navController
        binding.bottomNavView.setupWithNavController(navController)

        if(intent.getBooleanExtra(PlayerModule.ACTION_SONG_FRAG,false))
            navController.navigate(R.id.songFragment)

        lifecycleScope.launch {
            viewModel.collect(this@MainActivity) {

                it.apply(
                    binding,
                    imageLoader
                )
            }
        }

        binding.bottomPlayBtn.setOnClickListener {
            if ((it as ToggleButton).isChecked)
                viewModel.playerAction(
                    PlayerCommunicationState.Pause
                        (
                        MediaItem.Builder()
                            .setMediaMetadata(
                                MediaMetadata.Builder()
                                    .setTitle(binding.songNameTv.text.toString())
                                    .setArtist(binding.songAuthorName.text.toString())
                                    .build()
                            ).build()
                    )
                )
            else viewModel.playerAction(
                PlayerCommunicationState.Resume(
                    MediaItem.Builder()
                        .setMediaMetadata(
                            MediaMetadata.Builder()
                                .setTitle(binding.songNameTv.text.toString())
                                .setArtist(binding.songAuthorName.text.toString())
                                .build()
                        ).build()
                )
            )
        }

        binding.bottomPreviousBtn.setOnClickListener {
            viewModel.playerAction(PlayerCommunicationState.Previous)
        }

        binding.bottomNextBtn.setOnClickListener {
            viewModel.playerAction(PlayerCommunicationState.Next)
        }
    }

}