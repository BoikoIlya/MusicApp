package com.kamancho.melisma.frienddetails.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.ClickListener
import com.kamancho.melisma.app.core.Selector
import com.kamancho.melisma.main.di.App
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import com.kamancho.melisma.userplaylists.presentation.PlaylistsAdapter

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