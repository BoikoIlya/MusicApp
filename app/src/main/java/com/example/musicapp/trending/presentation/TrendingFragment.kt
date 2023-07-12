package com.example.musicapp.trending.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.R
import com.example.musicapp.main.di.App
import com.example.musicapp.app.core.Selector
import com.example.musicapp.databinding.TrendingFragmentBinding
import com.example.musicapp.trending.di.TrendingComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 25.01.2023.
 **/
class TrendingFragment: Fragment(R.layout.trending_fragment) {


    private val binding by viewBinding(TrendingFragmentBinding::bind)

    private lateinit var viewModel: TrendingViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var trendingComponent: TrendingComponent


    override fun onAttach(context: Context) {
        super.onAttach(context)
        trendingComponent = (context.applicationContext as App).appComponent.trendingComponent().build()
        trendingComponent.inject(this)

        viewModel = ViewModelProvider(this, factory)[TrendingViewModel::class.java]

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.collectState(this@TrendingFragment){

                    it.apply(
                        binding.errorLayout,
                        binding.trendingProgress,
                    )
            }
        }

        binding.rcvPlaylists.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false )

        val playlistsAdapter = PlaylistsAdapter(imageLoader,
            object : ClickListener<String> {
                override fun onClick(data: String) {
                    viewModel.savePlaylistId(data)
                    findNavController().navigate(R.id.action_trendingFragment_to_playlistFragment)
                }
            }
        )

        binding.rcvPlaylists.adapter = playlistsAdapter


        binding.errorLayout.reloadBtn.setOnClickListener {
            viewModel.loadData()
        }


        lifecycleScope.launch{
            viewModel.collectPlaylists(this@TrendingFragment){
                playlistsAdapter.map(it)
            }
        }

        binding.rcvTrendingTracks.layoutManager = LinearLayoutManager(requireContext())

        val tracksAdapter = TracksAdapter(
            this.requireContext(),
           playClickListener = object: Selector<MediaItem> {
                override fun onSelect(data: MediaItem, position: Int) {
                    viewModel.playMusic(data)
                }
            }, saveClickListener = object : ClickListener<MediaItem> {
            override fun onClick(data: MediaItem) {
                viewModel.checkAndAddTrackToFavorites(data)
            }
        }, imageLoader)

        binding.rcvTrendingTracks.adapter = tracksAdapter
        (binding.rcvTrendingTracks.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

        lifecycleScope.launch{
            viewModel.collectTracks(this@TrendingFragment){
                tracksAdapter.map(it)
                viewModel.saveCurrentPageQueue(it)
            }
        }

        lifecycleScope.launch{
            viewModel.collectSelectedTrack(this@TrendingFragment){
                tracksAdapter.newPosition(it)
            }
        }

        lifecycleScope.launch {
            viewModel.collectPlayerControls(this@TrendingFragment){
                it.apply(binding.rcvTrendingTracks)
            }
        }


    }
    }

