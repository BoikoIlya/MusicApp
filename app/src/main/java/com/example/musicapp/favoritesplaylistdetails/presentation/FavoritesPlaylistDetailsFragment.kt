package com.example.musicapp.favoritesplaylistdetails.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.Selector
import com.example.musicapp.main.di.App
import com.example.musicapp.favoritesplaylistdetails.di.FavoritesPlaylistDetailsComponent
import com.example.musicapp.trending.presentation.TracksAdapter
import kotlinx.coroutines.launch


class FavoritesPlaylistDetailsFragment: PlaylistDetailsFragment() {



    private lateinit var viewModel: FavoritesPlaylistDetailsViewModel

    private lateinit var playlistComponent: FavoritesPlaylistDetailsComponent

    override fun onAttach(context: Context) {
        super.onAttach(context)
        playlistComponent = (context.applicationContext as App).appComponent.playlistsComponent().build()
            .playlistDetailsComponent().build()
        playlistComponent.inject(this)

        viewModel = ViewModelProvider(this, factory)[FavoritesPlaylistDetailsViewModel::class.java]
        favoritesViewModel = viewModel
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.favoritesRcv.layoutManager = LinearLayoutManager(requireContext())
        val  tracksAdapter = TracksAdapter(
            context = requireContext(),
            playClickListener =  object : Selector<MediaItem> {
                override fun onSelect(data: MediaItem, position: Int) {
                    viewModel.playMusic(data)
                } },
            saveClickListener = object : ClickListener<MediaItem> {
                override fun onClick(data: MediaItem) {
                    viewModel.checkAndAddTrackToFavorites(data)
                } },

            imageLoader = imageLoader,
            cacheStrategy = DiskCacheStrategy.AUTOMATIC,
            addBtnVisibility = viewModel.addBtnVisibility(),
            layoutManager =  binding.favoritesRcv.layoutManager as RecyclerView.LayoutManager
        )
        adapter = tracksAdapter

        viewModel.isNotFollowed {
            val itemTouchHelper = ItemTouchHelper(
                ItemTouchHelperCallBackPlaylistDetails(
                    tracksAdapter,
                    (favoritesViewModel as FavoritesPlaylistDetailsViewModel),
                    requireContext()
                )
            )
            itemTouchHelper.attachToRecyclerView(binding.favoritesRcv)

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.collectDeleteDialogCommunication(this@FavoritesPlaylistDetailsFragment) {
                    itemTouchHelper.attachToRecyclerView(null)
                    itemTouchHelper.attachToRecyclerView(binding.favoritesRcv)
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

     override fun search(query: String) {
         viewModel.fetchFromCache(query)
            super.search(query)
     }



    override fun mainImageDiskCacheStrategy(): DiskCacheStrategy = DiskCacheStrategy.AUTOMATIC

 }