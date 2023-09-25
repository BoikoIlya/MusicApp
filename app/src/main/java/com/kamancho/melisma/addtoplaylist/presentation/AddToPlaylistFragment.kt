package com.kamancho.melisma.addtoplaylist.presentation

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kamancho.melisma.R
import com.kamancho.melisma.addtoplaylist.di.AddToPlaylistComponent
import com.kamancho.melisma.app.core.ClickListener
import com.kamancho.melisma.app.core.FavoritesFragment.Companion.loading_animation
import com.kamancho.melisma.app.core.FavoritesFragmentBottomSheet
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.favorites.data.SortingState
import com.kamancho.melisma.main.di.App
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kamancho.melisma.app.core.PagingListener
import com.kamancho.melisma.favorites.presentation.FavoritesTracksViewModel
import com.simform.refresh.SSPullToRefreshLayout
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 16.07.2023.
 **/
class AddToPlaylistFragment: FavoritesFragmentBottomSheet<SelectedTrackUi>(R.layout.favorites_fragment) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var component: AddToPlaylistComponent

    private lateinit var viewModel: AddToPlaylistViewModel


    override fun onAttach(context: Context) {
        super.onAttach(context)
        component = (context.applicationContext as App).appComponent
            .playlistDataComponent().build().addToPlaylistComponent().build()
        component.inject(this)

        viewModel = ViewModelProvider(this, factory)[AddToPlaylistViewModel::class.java]
        favoritesViewModel = viewModel
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        BottomSheetBehavior.from(view.parent as View).apply {
            state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.backBtn.setImageResource(R.drawable.check)
        binding.backBtn. imageTintList = ContextCompat.getColorStateList(requireContext(),R.color.black)
        binding.backBtn.visibility = View.VISIBLE
        binding.titleFavorites.setText(R.string.select_tracks)
        binding.shuffleFavorites.visibility = View.GONE


        binding.favoritesRcv.layoutManager = LinearLayoutManager(requireContext())

        val tracksAdapter = SelectedTracksAdapter(
            context = requireContext(),
             clickListener = object : ClickListener<SelectedTrackUi>{
                override fun onClick(data: SelectedTrackUi) {
                    viewModel.handleItemClick(data)
                }
            },
            imageLoader = imageLoader,
           )
        adapter = tracksAdapter
        binding.favoritesRcv.adapter = tracksAdapter
        binding.favoritesRcv.addOnScrollListener(PagingListener(30,15){
            viewModel.fetchData()
        })




        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.collectGlobalSingleUiEventCommunication(this@AddToPlaylistFragment){
                it.applyForBottomSheet(dialog!!.window!!.decorView,requireContext())
            }
        }

        binding.menu.setOnClickListener {
            val popup = PopupMenu(requireContext(), it,0, 0, R.style.popupOverflowMenu)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                popup.setForceShowIcon(true)
            }
            popup.menuInflater.inflate(R.menu.sort_options, popup.menu)
            popup.show()

            popup.setOnMenuItemClickListener { menuItem ->
                viewModel.resetOffset()
                when (menuItem.itemId) {
                    R.id.byTime -> viewModel.fetchData(SortingState.ByTime())
                    R.id.byName -> viewModel.fetchData(SortingState.ByName())
                    R.id.byArtist -> viewModel.fetchData(SortingState.ByArtist())
                }
                return@setOnMenuItemClickListener true
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }



    override fun search(query: String) {
        viewModel.resetOffset()
        viewModel.saveQuery(query)
        viewModel.fetchData()
    }

    override fun additionalActionsOnRecyclerUpdate(data: List<SelectedTrackUi>) = Unit

}