package com.example.musicapp.search.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.musicapp.R
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.ScrollUpRecyclerButton
import com.example.musicapp.app.core.Selector
import com.example.musicapp.main.di.App
import com.example.musicapp.search.di.SearchComponent
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import com.example.musicapp.userplaylists.presentation.PlaylistsViewHolder

/**
 * Created by HP on 14.08.2023.
 **/
class BaseSearchPlaylistsFragment: SearchListFragment<PlaylistUi, PlaylistsViewHolder>(){

    private lateinit var searchComponent: SearchComponent

    override fun onAttach(context: Context) {
        super.onAttach(context)
        searchComponent = (context.applicationContext as App).appComponent.searchComponent().build()
        searchComponent.inject(this)
        viewModel =  ViewModelProvider(this, factory)[BasePlaylistsListSearchViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.searchRcv.layoutManager = GridLayoutManager(requireContext(),2)
        val playlistsAdapter = SearchPlaylistsPagingAdapter(
            selectListener = object: Selector<PlaylistUi> {
                override fun onSelect(data: PlaylistUi, position: Int) = Unit
            }, clickClickListener = object : ClickListener<PlaylistUi> {
                override fun onClick(data: PlaylistUi) {
                    (viewModel as BasePlaylistsListSearchViewModel).savePlaylistData(data)
                    findNavController().navigate(R.id.action_searchFragment_to_searchPlaylistDetailsFragment)
                }
            }, imageLoader)
        adapter = playlistsAdapter


        super.onViewCreated(view, savedInstanceState)
    }


}