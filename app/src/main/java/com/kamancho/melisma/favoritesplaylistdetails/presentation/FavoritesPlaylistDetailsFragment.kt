package com.kamancho.melisma.favoritesplaylistdetails.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kamancho.melisma.app.core.ClickListener
import com.kamancho.melisma.app.core.Logger
import com.kamancho.melisma.app.core.Selector
import com.kamancho.melisma.main.di.App
import com.kamancho.melisma.favoritesplaylistdetails.di.FavoritesPlaylistDetailsComponent
import com.kamancho.melisma.trending.presentation.TracksAdapter
import kotlinx.coroutines.launch


class FavoritesPlaylistDetailsFragment: PlaylistDetailsFragment() {

    private val args: FavoritesPlaylistDetailsFragmentArgs by navArgs()

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.handlePlaylistData(args.playlistItem,savedInstanceState==null)
        viewModel.update(args.favoritePlaylistId,false,savedInstanceState==null)
        Logger.logFragment(
            findNavController().currentDestination?.label.toString(),
            requireContext()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        binding.favoritesRcv.layoutManager = LinearLayoutManager(requireContext())
        val tracksAdapter = TracksAdapter(
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
            addBtnVisibility = viewModel.addBtnVisibility(args.playlistItem),
            layoutManager =  binding.favoritesRcv.layoutManager as RecyclerView.LayoutManager
        )
        adapter = tracksAdapter

        viewModel.isNotFollowed(args.playlistItem) {
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

        binding.pullToRefresh.setOnRefreshListener{
            favoritesViewModel.update("", loading = true, shouldUpdate = true)
        }

    }

     override fun search(query: String) {
         viewModel.fetchFromCache(query)
            super.search(query)
     }



    override fun mainImageDiskCacheStrategy(): DiskCacheStrategy = DiskCacheStrategy.AUTOMATIC

 }