package com.kamancho.melisma.favorites.presentation

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.BlurEffectAnimator
import com.kamancho.melisma.app.core.ClickListener
import com.kamancho.melisma.app.core.FavoritesFragment
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.app.core.Selector
import com.kamancho.melisma.favorites.data.SortingState
import com.kamancho.melisma.favorites.di.FavoriteComponent
import com.kamancho.melisma.main.di.App
import com.kamancho.melisma.trending.presentation.*
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import javax.inject.Inject


/**
 * Created by HP on 26.01.2023.
 **/
class FavoritesTracksFragment: FavoritesFragment<MediaItem>(R.layout.favorites_fragment) {



    private lateinit var viewModel: FavoritesTracksViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var blurEffectAnimator: BlurEffectAnimator

    private lateinit var favoriteComponent: FavoriteComponent

    private lateinit var itemTouchHelper: ItemTouchHelper

    private lateinit var tracksAdapter: TracksAdapter

    private lateinit var layoutManager: LinearLayoutManager



    override fun onAttach(context: Context) {
        super.onAttach(context)
        favoriteComponent = (context.applicationContext as App).appComponent.favoriteComponent().build()
        favoriteComponent.inject(this)

        viewModel = ViewModelProvider(this, factory)[FavoritesTracksViewModel::class.java]
        super.favoritesViewModel = viewModel

    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.mainMenuBtn.visibility = View.VISIBLE

        layoutManager =  LinearLayoutManager(requireContext())
        binding.favoritesRcv.layoutManager = layoutManager

         tracksAdapter = TracksAdapter(
            this.requireContext(),
            playClickListener = object : Selector<MediaItem> {
                override fun onSelect(data: MediaItem, position: Int) {
                    viewModel.playMusic(data)
                }
            }, saveClickListener = object : ClickListener<MediaItem> {
                override fun onClick(data: MediaItem) {}
            }, imageLoader, View.GONE,
             navigator = object :Navigator{
                 override fun navigateToMenu(data: MediaItem, position: Int) {
                        viewModel.saveMediaItem(data)
                        findNavController().navigate(R.id.action_favoritesFragment_to_favoritesBottomSheetMenuFragment)
                 }
             },
             cacheStrategy = DiskCacheStrategy.AUTOMATIC,
             binding.favoritesRcv.layoutManager as LayoutManager
        )

        super.adapter = tracksAdapter

        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallBackFavorites(tracksAdapter, viewModel, requireContext()))
        itemTouchHelper.attachToRecyclerView(binding.favoritesRcv)
        binding.favoritesRcv.adapter = tracksAdapter




        lifecycleScope.launch {
            viewModel.collectSelectedTrack(this@FavoritesTracksFragment) {
                tracksAdapter.newPosition(it)
            }
        }


      viewLifecycleOwner.lifecycleScope.launch{
            viewModel.collectDeleteDialogCommunication(this@FavoritesTracksFragment){
                itemTouchHelper.attachToRecyclerView(null)
                itemTouchHelper.attachToRecyclerView(binding.favoritesRcv)
            }
        }

        lifecycleScope.launch{
            viewModel.collectDownloadCompleteCommunication(this@FavoritesTracksFragment){
                it.apply(viewModel)
            }
        }

        binding.shuffleFavorites.setOnClickListener {
            viewModel.shuffle()
        }

        binding.menu.setOnClickListener {
            blurEffectAnimator.show(view.rootView as View)
            val popup = PopupMenu(requireContext(), it,0, 0, R.style.popupOverflowMenu)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                popup.setForceShowIcon(true)
            }
            popup.menuInflater.inflate(R.menu.sort_options, popup.menu)
            popup.show()


            popup.setOnDismissListener { blurEffectAnimator.hide(view.rootView as View) }

            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.byTime -> viewModel.fetchData(SortingState.ByTime())
                    R.id.byName -> viewModel.fetchData(SortingState.ByName())
                    R.id.byArtist -> viewModel.fetchData(SortingState.ByArtist())
                }
                return@setOnMenuItemClickListener true
            }
        }

        binding.mainMenuBtn.setOnClickListener {
           findNavController().navigate(R.id.action_favoritesFragment_to_settingsFragment)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun additionalActionsOnRecyclerUpdate(data: List<MediaItem>) {
        viewModel.saveCurrentPageQueue(data)
    }



    override fun search(query: String) {
       viewModel.saveQuery(query)
        viewModel.fetchData()
    }


}