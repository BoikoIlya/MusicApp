package com.example.musicapp.frienddetails.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.musicapp.R
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.Selector
import com.example.musicapp.frienddetails.data.cache.FriendPlaylistsCacheToUiMapper
import com.example.musicapp.main.di.App
import com.example.musicapp.main.di.AppModule.Companion.mainPlaylistId
import com.example.musicapp.main.presentation.CollectSelectedTrack
import com.example.musicapp.trending.presentation.TracksAdapter
import com.example.musicapp.userplaylists.data.cache.PlaylistDao
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import com.example.musicapp.userplaylists.presentation.PlaylistsAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 18.08.2023.
 **/
class FriendPlaylistsFragment: FriendDataFragment<PlaylistUi>() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component = (context.applicationContext as App).appComponent.friendDetailsComponent().build()
        component.inject(this)
        viewModel = ViewModelProvider(this, factory)[FriendPlaylistsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.message.text = getString(R.string.no_favorite_playlists)
        binding.friendDetailsRcv.layoutManager = GridLayoutManager(requireContext(),2)

        val playlistsAdapter = PlaylistsAdapter(
            imageLoader =imageLoader,
            clickListener = object : ClickListener<PlaylistUi>{
                override fun onClick(data: PlaylistUi) {
                    (viewModel as FriendPlaylistsViewModel).savePlaylistData(data)
                    findNavController().navigate(R.id.action_friendDetailsFragment_to_searchPlaylistDetailsFragment)
                }
            },
            selector = object :Selector<PlaylistUi>{
                override fun onSelect(data: PlaylistUi, position: Int) = Unit
            }
        )


        binding.friendDetailsRcv.adapter = playlistsAdapter
        adapter = playlistsAdapter

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onRecyclerDataUpdate(data: List<PlaylistUi>) = Unit


}