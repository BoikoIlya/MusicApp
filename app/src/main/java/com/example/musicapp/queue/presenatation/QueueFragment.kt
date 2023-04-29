package com.example.musicapp.queue.presenatation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicapp.R
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.app.core.Selector
import com.example.musicapp.databinding.PlayerFragmentBinding
import com.example.musicapp.databinding.QueueFragmentBinding
import com.example.musicapp.main.di.App
import com.example.musicapp.main.presentation.MainActivity
import com.example.musicapp.main.presentation.PlayerCommunicationState
import com.example.musicapp.player.di.PlayerComponent
import com.example.musicapp.player.presentation.PlayerViewModel
import com.example.musicapp.queue.di.QueueComponent
import com.example.musicapp.trending.presentation.TracksAdapter
import com.example.musicapp.trending.presentation.TrendingViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 22.04.2023.
 **/
class QueueFragment: Fragment(R.layout.queue_fragment) {

    private val binding by viewBinding(QueueFragmentBinding::bind)

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var viewModel: QueueViewModel


    private lateinit var playerComponent: QueueComponent


    override fun onAttach(context: Context) {
        super.onAttach(context)
        playerComponent = (context.applicationContext as App).appComponent.queueComponent().build()
        playerComponent.inject(this)
        viewModel =  ViewModelProvider(this, factory)[QueueViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.queueRcv.layoutManager = LinearLayoutManager(requireContext())

        val tracksAdapter = TracksAdapter(
            requireContext(),
            playClickListener = object : Selector<MediaItem> {
                override fun onSelect(data: MediaItem, position: Int) {
                    viewModel.playerAction(PlayerCommunicationState.Play(data, position))
                }
            }, saveClickListener = object : ClickListener<MediaItem> {
                override fun onClick(data: MediaItem) {}
            }, imageLoader, View.GONE
        )

        lifecycleScope.launch{
            viewModel.collectCurrentQueue(this@QueueFragment){
                tracksAdapter.map(it)
            }
        }

        lifecycleScope.launch{
            viewModel.collectSelectedTrack(this@QueueFragment){
                tracksAdapter.newPosition(it)
            }
        }

        binding.queueRcv.adapter = tracksAdapter

        binding.trackPosition.setOnClickListener {
            tracksAdapter.scrollToSelectedTrack(binding.queueRcv)
        }

        binding.moveUp.setOnClickListener {
            binding.queueRcv.smoothScrollToPosition(0)
        }

        binding.backBtnQueue.setOnClickListener{
            viewModel.slidePage(0)
        }
    }
}