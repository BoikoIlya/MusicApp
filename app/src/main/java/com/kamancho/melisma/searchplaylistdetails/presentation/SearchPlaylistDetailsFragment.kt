package com.kamancho.melisma.searchplaylistdetails.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.ClickListener
import com.kamancho.melisma.app.core.Selector
import com.kamancho.melisma.favoritesplaylistdetails.presentation.PlaylistDetailsFragment
import com.kamancho.melisma.main.di.App
import com.kamancho.melisma.searchplaylistdetails.di.SearchPlaylistDetailsComponent
import com.kamancho.melisma.trending.presentation.TracksAdapter
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 15.08.2023.
 **/
class SearchPlaylistDetailsFragment: PlaylistDetailsFragment() {

    private lateinit var component: SearchPlaylistDetailsComponent

    private val args: SearchPlaylistDetailsFragmentArgs by navArgs()


    override fun onAttach(context: Context) {
        component = (context.applicationContext as App).appComponent.playlistDataComponent().build()
            .searchPlaylistDetailsComponent().build()
        component.inject(this)
        favoritesViewModel = ViewModelProvider(this, factory)[SearchPlaylistDetailsViewModel::class.java]
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (favoritesViewModel as SearchPlaylistDetailsViewModel)
            .initPlaylistData(args.playlistItem,savedInstanceState==null)

        (favoritesViewModel as SearchPlaylistDetailsViewModel)
            .update(args.playlistItem,false,savedInstanceState==null)
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
            (favoritesViewModel as SearchPlaylistDetailsViewModel).followPlaylist(args.playlistItem)
        }

        super.onViewCreated(view, savedInstanceState)

        binding.pullToRefresh.setOnRefreshListener{
            favoritesViewModel.update(args.playlistItem, loading = true, shouldUpdate = true)
        }
    }

    override fun search(query: String) {
        (favoritesViewModel as SearchPlaylistDetailsViewModel).find(query)
        super.search(query)
    }

    override fun mainImageDiskCacheStrategy(): DiskCacheStrategy = DiskCacheStrategy.NONE


}