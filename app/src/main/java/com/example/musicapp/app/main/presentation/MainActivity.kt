package com.example.musicapp.app.main.presentation

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
import com.example.musicapp.app.main.di.App
import com.example.musicapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    lateinit var viewModel: MainActivityViewModel

    val player = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     binding = ActivityMainBinding.inflate(layoutInflater)

        (this.application as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this,factory)[MainActivityViewModel::class.java]

        setContentView(binding.root)


        val navHost = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHost.navController
        binding.bottomNavView.setupWithNavController(navController)

        lifecycleScope.launch{
            viewModel.collect(this@MainActivity){
                it.apply(
                    binding,
                    imageLoader
                )
            }
        }

        binding.bottomPlayBtn.setOnClickListener {
            if((it as ToggleButton).isChecked)
                viewModel.playerAction(PlayerCommunicationState.Pause
                    (
                    MediaItem.Builder()
                        .setMediaMetadata(MediaMetadata.Builder()
                            .setTitle(binding.songNameTv.text.toString())
                            .setArtist(binding.songAuthorName.text.toString())
                            .build()).build()
                    )
                )
            else viewModel.playerAction(PlayerCommunicationState.Resume(
                MediaItem.Builder()
                    .setMediaMetadata(MediaMetadata.Builder()
                        .setTitle(binding.songNameTv.text.toString())
                        .setArtist(binding.songAuthorName.text.toString())
                        .build()).build()
            ))
        }
    }


    override fun onDestroy() {
        //viewModel.playerAction(PlayerCommunicationState.)
        super.onDestroy()
    }
}