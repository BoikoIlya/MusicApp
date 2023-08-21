package com.example.musicapp.search.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.ScrollUpRecyclerButton
import com.example.musicapp.app.core.Selector
import com.example.musicapp.main.di.App
import com.example.musicapp.search.di.SearchComponent
import com.example.musicapp.trending.presentation.TracksViewHolder
import kotlinx.coroutines.launch

/**
 * Created by HP on 14.08.2023.
 **/
class BaseSearchTracksFragment: SearchListFragment<MediaItem, TracksViewHolder>(){

    private lateinit var searchComponent: SearchComponent

    override fun onAttach(context: Context) {
        super.onAttach(context)
        searchComponent = (context.applicationContext as App).appComponent.searchComponent().build()
        searchComponent.inject(this)
        viewModel =  ViewModelProvider(this, factory)[BaseTracksListSearchViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.searchRcv.layoutManager = LinearLayoutManager(requireContext())

        val tracksAdapter = SearchTracksPagingAdapter(  this.requireContext(),
            playClickListener = object: Selector<MediaItem> {
                override fun onSelect(data: MediaItem, position: Int) {
                    viewModel.playMusic(data)
                }
            }, saveClickListener = object : ClickListener<MediaItem> {
                override fun onClick(data: MediaItem) {
                    viewModel.checkAndAddTrackToFavorites(data)
                }
            }, imageLoader)
        adapter = tracksAdapter


        lifecycleScope.launch{
            viewModel.collectSelectedTrack(this@BaseSearchTracksFragment){
                tracksAdapter.newPosition(it)
            }
        }


        super.onViewCreated(view, savedInstanceState)
    }



}