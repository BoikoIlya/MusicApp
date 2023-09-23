package com.kamancho.melisma.favoritesplaylistdetails.presentation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.recyclerview.widget.ConcatAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.FavoritesFragment
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.trending.presentation.TracksAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 15.08.2023.
 **/
abstract class PlaylistDetailsFragment: FavoritesFragment<MediaItem>(R.layout.favorites_fragment) {


    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var barAdapter: PlaylistDetailsBarAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.titleFavorites.visibility = View.GONE
        binding.shuffleFavorites.visibility = View.GONE
        binding.menu.visibility = View.GONE
        binding.backBtn.visibility = View.VISIBLE

        barAdapter = PlaylistDetailsBarAdapter(requireContext(),imageLoader, mainImageDiskCacheStrategy())

        val contactedAdapter =  ConcatAdapter(barAdapter,adapter as TracksAdapter)
        binding.favoritesRcv.adapter = contactedAdapter

        lifecycleScope.launch{
            (favoritesViewModel as PlaylistDetailsViewModel).collectSelectedTrack(this@PlaylistDetailsFragment){
                (adapter as TracksAdapter).newPosition(it)
            }
        }



        viewLifecycleOwner.lifecycleScope.launch{
            (favoritesViewModel as PlaylistDetailsViewModel).collectPlaylistDataCommunication(this@PlaylistDetailsFragment){
                barAdapter.setupPlaylist(it)
                barAdapter.showTopBar(binding.searchFavorites.text.isEmpty())
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun search(query: String) {
        barAdapter.showTopBar(query.isEmpty())
    }

    override fun additionalActionsOnRecyclerUpdate(data: List<MediaItem>) {
        (favoritesViewModel as PlaylistDetailsViewModel).saveCurrentPageQueue(data)
    }



    protected abstract fun mainImageDiskCacheStrategy(): DiskCacheStrategy
}