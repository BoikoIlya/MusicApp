package com.example.musicapp.favorites.presentation

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.R
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.app.core.Selector
import com.example.musicapp.databinding.FavorotesFragmentBinding
import com.example.musicapp.favorites.data.SortingState
import com.example.musicapp.favorites.di.FavoriteComponent
import com.example.musicapp.main.di.App
import com.example.musicapp.trending.presentation.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@UnstableApi /**
 * Created by HP on 26.01.2023.
 **/
class FavoritesFragment: Fragment() {


    private lateinit var binding: FavorotesFragmentBinding

    private lateinit var viewModel: FavoritesViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader


    private lateinit var favoriteComponent: FavoriteComponent

    private lateinit var textWatcher: TextWatcher

    private lateinit var itemTouchHelperCallBack: ItemTouchHelperCallBack

    private lateinit var tracksAdapter: TracksAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        favoriteComponent = (context.applicationContext as App).appComponent.favoriteComponent().build()
        favoriteComponent.inject(this)

        viewModel = ViewModelProvider(this, factory)[FavoritesViewModel::class.java]

        textWatcher = object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.saveQuery(text.toString())
                viewModel.fetchData()
            }

            override fun afterTextChanged(p0: Editable?) = Unit
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FavorotesFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.collectState(this@FavoritesFragment) {
                it.apply(
                    binding.noFavoriteTracks,
                    binding.favoritesProgress,
                    binding.favoritesRcv
                )
            }
        }


        binding.favoritesRcv.layoutManager = LinearLayoutManager(requireContext())

         tracksAdapter = TracksAdapter(
            this.requireContext(),
            playClickListener = object : Selector<MediaItem> {
                override fun onSelect(data: MediaItem, position: Int) {
                    viewModel.playMusic(data)
                }
            }, saveClickListener = object : ClickListener<MediaItem> {
                override fun onClick(data: MediaItem) {}
            }, imageLoader, View.GONE
        )

        (binding.favoritesRcv.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        itemTouchHelperCallBack = ItemTouchHelperCallBack(tracksAdapter, viewModel, requireContext())
        ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(binding.favoritesRcv)

        binding.favoritesRcv.adapter = tracksAdapter

        lifecycleScope.launch {
            viewModel.collectTracks(this@FavoritesFragment) {
                tracksAdapter.map(it)
                viewModel.saveCurrentPageQueue(it)
                binding.favoritesRcv.scrollToPosition(0)

            }
        }


        lifecycleScope.launch {
            viewModel.collectSelectedTrack(this@FavoritesFragment) {
                tracksAdapter.newPosition(it)
            }
        }

        lifecycleScope.launch {
            viewModel.collectPlayerControls(this@FavoritesFragment){
                it.apply(binding.favoritesRcv)
            }
        }


        binding.shuffleFavorites.setOnClickListener {
            viewModel.shuffle()
        }


        binding.menu.setOnClickListener {
            val popup = PopupMenu(requireContext(), it,0, 0, R.style.popupOverflowMenu)
            popup.menuInflater.inflate(R.menu.sort_options, popup.menu)
            popup.show()
            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.byTime -> viewModel.fetchData(SortingState.ByTime())
                    R.id.byName -> viewModel.fetchData(SortingState.ByName())
                    R.id.byArtist -> viewModel.fetchData(SortingState.ByArtist())
                }
                return@setOnMenuItemClickListener true
            }
        }

    }

    override fun onStart() {
        binding.searchFavorites.addTextChangedListener(textWatcher)
        super.onStart()
    }


    override fun onStop() {
        binding.searchFavorites.removeTextChangedListener(textWatcher)
        super.onStop()
    }

}