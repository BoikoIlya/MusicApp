package com.example.musicapp.frienddetails.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.musicapp.R
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.Selector
import com.example.musicapp.favoritesplaylistdetails.presentation.FavoritesPlaylistDetailsViewModel
import com.example.musicapp.main.di.App
import com.example.musicapp.main.presentation.CollectSelectedTrack
import com.example.musicapp.trending.presentation.TracksAdapter
import kotlinx.coroutines.launch

/**
 * Created by HP on 19.08.2023.
 **/
class FriendTracksFragment: FriendDataFragment<MediaItem>() {


    override fun onAttach(context: Context) {
        super.onAttach(context)
        component = (context.applicationContext as App).appComponent.friendDetailsComponent().build()
        component.inject(this)
        viewModel = ViewModelProvider(this, factory)[FriendTracksViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.message.text = getString(R.string.no_favorite_tracks)
        binding.friendDetailsRcv.layoutManager = LinearLayoutManager(requireContext())

        val tracksAdapter = TracksAdapter(
            context =requireContext(),
            saveClickListener = object: ClickListener<MediaItem> {
                override fun onClick(data: MediaItem) {
                    (viewModel as FriendTracksViewModel).checkAndAddTrackToFavorites(data)
                }
            },
            playClickListener = object : Selector<MediaItem> {
                override fun onSelect(data: MediaItem, position: Int) {
                    (viewModel as FriendTracksViewModel).playMusic(data)
                }
            },
            imageLoader = imageLoader,
            addBtnVisibility = View.VISIBLE,
            cacheStrategy = DiskCacheStrategy.AUTOMATIC,
            layoutManager =  binding.friendDetailsRcv.layoutManager as RecyclerView.LayoutManager
        )
        binding.friendDetailsRcv.adapter = tracksAdapter
        adapter = tracksAdapter


        lifecycleScope.launch {
            (viewModel as CollectSelectedTrack).collectSelectedTrack(this@FriendTracksFragment) {
                tracksAdapter.newPosition(it)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onRecyclerDataUpdate(data: List<MediaItem>) {
        (viewModel as FriendTracksViewModel).saveCurrentPageQueue(data)
    }
}