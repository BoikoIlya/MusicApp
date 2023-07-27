package com.example.musicapp.selectplaylist.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.musicapp.R
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.FavoritesFragment
import com.example.musicapp.app.core.FavoritesFragmentBottomSheet
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.app.core.Selector
import com.example.musicapp.main.di.App
import com.example.musicapp.selectplaylist.di.SelectPlaylistComponent
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import com.example.musicapp.userplaylists.presentation.PlaylistsAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 19.07.2023.
 **/
class SelectPlaylistFragment: FavoritesFragmentBottomSheet<PlaylistUi>(R.layout.favorotes_fragment) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var component: SelectPlaylistComponent

    private lateinit var viewModel: SelectPlaylistViewModel




    override fun onAttach(context: Context) {
        super.onAttach(context)
        component = (context.applicationContext as App).appComponent.selectPlaylistComponent().build()
        component.inject(this)

        viewModel = ViewModelProvider(requireActivity(), factory)[SelectPlaylistViewModel::class.java]
        super.favoritesViewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        BottomSheetBehavior.from(view.parent as View).apply {
            state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.backBtn.visibility = View.VISIBLE
        binding.shuffleFavorites.visibility = View.INVISIBLE
        binding.menu.setImageResource(R.drawable.plus)
        binding.titleFavorites.setText(R.string.select_playlist)
        binding.noData.setText(R.string.no_favorite_playlists)

        val paddingInDpHorizontal = 10
        val paddingInDpBottom = 140
        val scale = resources.displayMetrics.density
        val paddingInPxHorizontal = (paddingInDpHorizontal * scale + 0.5f).toInt()
        val paddingInPxBottom = (paddingInDpBottom * scale + 0.5f).toInt()

        binding.favoritesRcv.setPadding(paddingInPxHorizontal, 0, paddingInPxHorizontal, paddingInPxBottom)

        val layoutManager =  GridLayoutManager(requireContext(),2)
        binding.favoritesRcv.layoutManager = layoutManager

        val playlistsAdapter = PlaylistsAdapter(imageLoader,
            clickListener = object : ClickListener<PlaylistUi> {
                override fun onClick(data: PlaylistUi) {
                    findNavController().popBackStack()
                    findNavController().popBackStack()
                    viewModel.addToPlaylist(data)

                }
            },
            selector = object : Selector<PlaylistUi> {
                override fun onSelect(data: PlaylistUi, position: Int) = Unit
            })
        super.adapter = playlistsAdapter
        binding.favoritesRcv.adapter = playlistsAdapter



        binding.menu.setOnClickListener {
            findNavController().navigate(R.id.action_selectPlaylistFragment_to_createPlaylistFragment)
        }

        super.onViewCreated(view, savedInstanceState)
    }


    override fun onStart() {
        super.onStart()
        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.collectGlobalSingleUiEventCommunication(this@SelectPlaylistFragment){
                Log.d("tag", "apply: BOTTOM")
                it.applyForBottomSheet(dialog!!.window!!.decorView,requireContext())
            }
        }
    }

    override fun search(query: String) = viewModel.fetch(query)


    override fun additionalActionsOnRecyclerUpdate(data: List<PlaylistUi>) = Unit
    
}