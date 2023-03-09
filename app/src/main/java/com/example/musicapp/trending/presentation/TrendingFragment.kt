package com.example.musicapp.trending.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.R
import com.example.musicapp.app.main.di.App
import com.example.musicapp.app.main.presentation.ItemPositionState
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.Selector
import com.example.musicapp.databinding.TrendingFragmentBinding
import com.example.musicapp.trending.di.TrendingComponent
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 25.01.2023.
 **/
class TrendingFragment: Fragment(R.layout.trending_fragment) {


    private lateinit var binding: TrendingFragmentBinding

    private lateinit var viewModel: TrendingViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var mapper: TrackUi.Mapper<TrackUi>

    @Inject
    lateinit var managerResource: ManagerResource

    private lateinit var trendingComponent: TrendingComponent

    override fun onAttach(context: Context) {
        super.onAttach(context)
        trendingComponent = (context.applicationContext as App).appComponent.trendingComponent().build()
        trendingComponent.inject(this)

        viewModel = ViewModelProvider(this, factory)[TrendingViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TrendingFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launch {
            viewModel.collectState(this@TrendingFragment){
                it.apply(
                    binding.errorImg,
                    binding.errorTv,
                    binding.reloadBtn,
                    binding.trendingProgress
                )
            }
        }

        binding.rcvPlaylists.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false )

        val playlistsAdapter = PlaylistsAdapter(imageLoader,
            object : ClickListener<PlaylistUi> {
                override fun onClick(data: PlaylistUi) {
                    //TODO
                }

            }
        )

        binding.rcvPlaylists.adapter = playlistsAdapter


        binding.reloadBtn.setOnClickListener {
            viewModel.loadData()
        }


        lifecycleScope.launch{
            viewModel.collectPlaylists(this@TrendingFragment){
                playlistsAdapter.map(it)
            }
        }

        binding.rcvTrendingTracks.layoutManager = LinearLayoutManager(requireContext())

        val tracksAdapter = TrendingTracksAdapter(
            this.requireContext(),
           playClickListener = object: Selector<MediaItem> {
                override fun onSelect(data: MediaItem, position: Int) {
                    viewModel.playMusic(data, position)
                }
            }, saveClickListener = object : ClickListener<MediaItem> {
            override fun onClick(data: MediaItem) {
                //TODO
            }
        },mapper)

        binding.rcvTrendingTracks.adapter = tracksAdapter

        lifecycleScope.launch{
            viewModel.collectTracks(this@TrendingFragment){
                tracksAdapter.map(it)
                viewModel.setQuery(it)
            }
        }

        lifecycleScope.launch{
            viewModel.collectSelectedTrackPosition(this@TrendingFragment){
              //  it.apply(tracksAdapter)
                tracksAdapter.newPosition(it)
            }
        }

    }
    }

