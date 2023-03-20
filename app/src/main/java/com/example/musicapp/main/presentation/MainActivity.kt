package com.example.musicapp.main.presentation

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.musicapp.R
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.main.di.App
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.player.di.PlayerModule
import com.example.musicapp.player.presentation.PlayerActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    lateinit var viewModel: MainViewModel

    val player = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(intent.getBooleanExtra(PlayerModule.ACTION_SONG_ACT,false))
            startActivity(Intent(this,PlayerActivity::class.java))

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

        lifecycleScope.launch {
            viewModel.collect(this@MainActivity) {

                it.apply(
                    binding,
                    imageLoader
                )
            }
        }

        binding.playBtn.setOnClickListener {
            if ((it as ToggleButton).isChecked)
                viewModel.playerAction(PlayerCommunicationState.Pause)
            else
                viewModel.playerAction(PlayerCommunicationState.Resume)
        }

        binding.previousBtn.setOnClickListener {
            viewModel.playerAction(PlayerCommunicationState.Previous)
        }

        binding.nextBtn.setOnClickListener {
            viewModel.playerAction(PlayerCommunicationState.Next)
        }

        binding.bottomPlayerBar.setOnClickListener {
            startActivity(Intent(this,PlayerActivity::class.java))
        }
    }

}