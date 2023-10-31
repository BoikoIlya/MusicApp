package com.kamancho.melisma.frienddetails.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.ClickListener
import com.kamancho.melisma.app.core.Selector
import com.kamancho.melisma.main.di.App
import com.kamancho.melisma.userplaylists.presentation.FavoritesPlaylistsFragment
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import com.kamancho.melisma.userplaylists.presentation.PlaylistsAdapter
import kotlin.math.log10

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.update(requireArguments().getString(ARG_KEY)!!,false,savedInstanceState==null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        binding.message.text = getString(R.string.no_favorite_playlists)
        layoutManager = GridLayoutManager(requireContext(),2)
        binding.friendDetailsRcv.layoutManager = layoutManager

        val playlistsAdapter = PlaylistsAdapter(
            imageLoader =imageLoader,
            clickListener = object : ClickListener<PlaylistUi>{
                override fun onClick(data: PlaylistUi) {


                    val handledId = data.handleId()
                    val bundle = Bundle()
                    bundle.putParcelable(FavoritesPlaylistsFragment.playlist_key,handledId)
                    findNavController().navigate(R.id.action_friendDetailsFragment_to_searchPlaylistDetailsFragment,bundle)
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

    override fun getFriendId(): String = requireArguments().getString(ARG_KEY)!!

    companion object {
        private const val ARG_KEY = "argument_key"

        fun newInstance(friendId: String): FriendPlaylistsFragment {
            val fragment = FriendPlaylistsFragment()
            val args = Bundle()
            args.putString(ARG_KEY, friendId)
            fragment.arguments = args
            return fragment
        }

    }
}