package com.kamancho.melisma.userplaylists.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.ClickListener
import com.kamancho.melisma.app.core.FavoritesFragment
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.app.core.Selector
import com.kamancho.melisma.main.di.App
import com.kamancho.melisma.userplaylists.di.PlaylistsComponent
import javax.inject.Inject

/**
 * Created by HP on 13.07.2023.
 **/
open class FavoritesPlaylistsFragment: FavoritesFragment<PlaylistUi>(R.layout.favorites_fragment) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var playlistsComponent: PlaylistsComponent

    private lateinit var viewModel: PlaylistsViewModel


    override fun onAttach(context: Context) {
        super.onAttach(context)
        playlistsComponent = (context.applicationContext as App).appComponent.playlistsComponent().build()
        playlistsComponent.inject(this)

        viewModel = ViewModelProvider(this, factory)[PlaylistsViewModel::class.java]
        super.favoritesViewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.backBtn.visibility = View.VISIBLE
        binding.shuffleFavorites.visibility = View.GONE
        binding.titleFavorites.setText(R.string.favorites_playlists)
        binding.errorMessage.setText(R.string.no_favorite_playlists)
        binding.menu.setImageResource(R.drawable.plus)

        val paddingInDpHorizontal = 10
        val paddingInDpBottom = 140
        val scale = resources.displayMetrics.density
        val paddingInPxHorizontal = (paddingInDpHorizontal * scale + 0.5f).toInt()
        val paddingInPxBottom = (paddingInDpBottom * scale + 0.5f).toInt()

        binding.favoritesRcv.setPadding(paddingInPxHorizontal, 0, paddingInPxHorizontal, paddingInPxBottom)

        val layoutManager =  GridLayoutManager(requireContext(),2)
        binding.favoritesRcv.layoutManager = layoutManager

        val playlistsAdapter = PlaylistsAdapter(imageLoader,
            clickListener = object : ClickListener<PlaylistUi>{
                override fun onClick(data: PlaylistUi) {
                    //todo maybe need
                    viewModel.savePlaylist(data)

                    val bundle = Bundle()
                    bundle.putParcelable(playlist_key,data)
                    bundle.putString(playlist_id_key,data.map(PlaylistUi.ToIdMapper()))
                    findNavController().navigate(R.id.action_playlistsFragment_to_favoritesPlaylistDetailsFragment,bundle)
                }
            },
            selector = object : Selector<PlaylistUi>{
                override fun onSelect(data: PlaylistUi, position: Int) {
                    viewModel.savePlaylist(data)

                    val bundle = Bundle()
                    bundle.putParcelable(playlist_key,data)

                    findNavController().navigate(R.id.action_playlistsFragment_to_playlistsMenuDialogBottomSheetFragment,bundle)
                }
            })
        super.adapter = playlistsAdapter
        binding.favoritesRcv.adapter = playlistsAdapter

        binding.menu.setOnClickListener {
            findNavController().navigate(R.id.action_playlistsFragment_to_createPlaylistFragment)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    companion object{
         const val playlist_key = "playlistItem"
        private const val playlist_id_key = "favoritePlaylistId"
    }



    override fun search(query: String) = viewModel.fetch(query)


    override fun additionalActionsOnRecyclerUpdate(data: List<PlaylistUi>) = Unit
}