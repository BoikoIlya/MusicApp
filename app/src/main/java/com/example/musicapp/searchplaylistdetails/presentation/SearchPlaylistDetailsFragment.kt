package com.example.musicapp.searchplaylistdetails.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.musicapp.R
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.Selector
import com.example.musicapp.favoritesplaylistdetails.presentation.PlaylistDetailsFragment
import com.example.musicapp.main.di.App
import com.example.musicapp.searchplaylistdetails.di.SearchPlaylistDetailsComponent
import com.example.musicapp.trending.presentation.TracksAdapter

/**
 * Created by HP on 15.08.2023.
 **/
class SearchPlaylistDetailsFragment: PlaylistDetailsFragment() {

    private lateinit var component: SearchPlaylistDetailsComponent

    override fun onAttach(context: Context) {
        component = (context.applicationContext as App).appComponent.playlistDataComponent().build()
            .searchPlaylistDetailsComponent().build()
        component.inject(this)
        favoritesViewModel = ViewModelProvider(this, factory)[SearchPlaylistDetailsViewModel::class.java]
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.mainMenuBtn.setImageResource(R.drawable.plus)
        binding.mainMenuBtn.visibility = View.VISIBLE

        binding.favoritesRcv.layoutManager = LinearLayoutManager(requireContext())
        val tracksAdapter = TracksAdapter(
            context = requireContext(),
            playClickListener = object : Selector<MediaItem> {
                override fun onSelect(data: MediaItem, position: Int) {
                    (favoritesViewModel as SearchPlaylistDetailsViewModel).playMusic(data)
                } },
            saveClickListener = object : ClickListener<MediaItem> {
                override fun onClick(data: MediaItem) {
                    (favoritesViewModel as SearchPlaylistDetailsViewModel).checkAndAddTrackToFavorites(data)
                } },

            imageLoader = imageLoader,
            cacheStrategy = DiskCacheStrategy.NONE,
            addBtnVisibility = View.VISIBLE,
            layoutManager =  binding.favoritesRcv.layoutManager as RecyclerView.LayoutManager
        )
        adapter = tracksAdapter


        binding.mainMenuBtn.setOnClickListener {
            (favoritesViewModel as SearchPlaylistDetailsViewModel).followPlaylist()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun search(query: String) {
        (favoritesViewModel as SearchPlaylistDetailsViewModel).find(query)
        super.search(query)
    }

    override fun mainImageDiskCacheStrategy(): DiskCacheStrategy = DiskCacheStrategy.NONE


}