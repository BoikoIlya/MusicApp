package com.kamancho.melisma.search.presentation

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
import com.kamancho.melisma.app.vkdto.SearchPlaylistItem
import com.kamancho.melisma.main.di.App
import com.kamancho.melisma.search.di.SearchComponent
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import com.kamancho.melisma.userplaylists.presentation.FavoritesPlaylistsFragment.Companion.playlist_key
import com.kamancho.melisma.userplaylists.presentation.PlaylistThumbsState
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import com.kamancho.melisma.userplaylists.presentation.PlaylistsAdapter
import com.kamancho.melisma.userplaylists.presentation.PlaylistsViewHolder

/**
 * Created by HP on 14.08.2023.
 **/
class BaseSearchPlaylistsFragment: SearchListFragment<PlaylistUi, PlaylistsViewHolder,PlaylistDomain,SearchPlaylistItem>(){

    private lateinit var searchComponent: SearchComponent

    override fun onAttach(context: Context) {
        super.onAttach(context)
        searchComponent = (context.applicationContext as App).appComponent.searchComponent().build()
        searchComponent.inject(this)
        viewModel =  ViewModelProvider(this, factory)[BasePlaylistsListSearchViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val layoutManager = GridLayoutManager(requireContext(),recycler_span_count)
        binding.searchRcv.layoutManager = layoutManager



        val playlistsAdapter = PlaylistsAdapter(
            imageLoader,
            clickListener = object : ClickListener<PlaylistUi>{
                override fun onClick(data: PlaylistUi) {

                    val bundle = Bundle()

                    bundle.putParcelable(playlist_key,data)
                    findNavController().navigate(R.id.action_searchFragment_to_searchPlaylistDetailsFragment,bundle)
                }
            }, selector = object : Selector<PlaylistUi>{
                override fun onSelect(data: PlaylistUi, position: Int) = Unit
            }
        )


        adapter = playlistsAdapter


        super.onViewCreated(view, savedInstanceState)

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int =
                if(position == concatAdapter.itemCount-1 && defaultLoadStateAdapter.itemCount>0)
                    recycler_span_count
                else 1
        }
    }

    override fun getQuery(): String = requireArguments().getString(ARG_KEY)?:""

    companion object {
        private const val recycler_span_count = 2
        private const val ARG_KEY = "argument_key"

        fun newInstance(query: String): BaseSearchPlaylistsFragment {
            val fragment = BaseSearchPlaylistsFragment()
            val args = Bundle()
            args.putString(ARG_KEY, query)
            fragment.arguments = args
            return fragment
        }

    }

}