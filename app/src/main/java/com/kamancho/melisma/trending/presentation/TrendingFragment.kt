package com.kamancho.melisma.trending.presentation

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kamancho.melisma.app.core.ClickListener
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.FavoritesFragment
import com.kamancho.melisma.main.di.App
import com.kamancho.melisma.app.core.Selector
import com.kamancho.melisma.databinding.TrendingFragmentBinding
import com.kamancho.melisma.trending.di.TrendingComponent
import com.kamancho.melisma.userplaylists.presentation.PlaylistSectionAdapter
import com.simform.refresh.SSPullToRefreshLayout
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 25.01.2023.
 **/
class TrendingFragment: Fragment(R.layout.trending_fragment) {


    private val binding by viewBinding(TrendingFragmentBinding::bind)

    private lateinit var viewModel: TrendingViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var trendingComponent: TrendingComponent


    override fun onAttach(context: Context) {
        super.onAttach(context)
        trendingComponent = (context.applicationContext as App).appComponent.trendingComponent().build()
        trendingComponent.inject(this)

        viewModel = ViewModelProvider(this, factory)[TrendingViewModel::class.java]

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pullToRefresh.setRepeatCount(SSPullToRefreshLayout.RepeatCount.INFINITE)
        binding.pullToRefresh.setLottieAnimation(FavoritesFragment.loading_animation)
        binding.pullToRefresh.setRefreshViewParams(
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                250
            ))



        val playlistsAdapter = TrendingTopBarAdapter(imageLoader,findNavController())
        binding.trendingRcv.layoutManager = LinearLayoutManager(requireContext())
        val tracksAdapter = TracksAdapter(
            this.requireContext(),
            playClickListener = object: Selector<MediaItem> {
                override fun onSelect(data: MediaItem, position: Int) {
                    viewModel.playMusic(data)
                }
            }, saveClickListener = object : ClickListener<MediaItem> {
                override fun onClick(data: MediaItem) {
                    viewModel.checkAndAddTrackToFavorites(data)
                }
            }, imageLoader,
            layoutManager =  binding.trendingRcv.layoutManager as RecyclerView.LayoutManager
            )


        val dpToPx = resources.displayMetrics.density

        val mainTitleAdapter =
            TitleAdapter(
                text = getString(R.string.trending_header),
                textSize = resources.getDimension(R.dimen.h1)/ resources.displayMetrics.scaledDensity,
                margins = listOf(25,25,0,20),
                typeface = Typeface.BOLD,
                dpToPx = dpToPx
            )

        val recommendationsTitleAdapter =
            TitleAdapter(
                text = getString(R.string.top_200_recommendations),
                textSize = resources.getDimension(R.dimen.h3)/ resources.displayMetrics.scaledDensity,
                margins = listOf(25,20,0,15),
                typeface = Typeface.NORMAL,
                dpToPx = dpToPx
                )



        val playlistSectionAdapter = PlaylistSectionAdapter(
            playlistAdapter = playlistsAdapter,
            context = requireContext()
        )

        val embededVkPlaylistsAdapter = TrendingTopBarAdapter(imageLoader,findNavController())

        val secondPlaylistSectionAdapter = PlaylistSectionAdapter(
            playlistAdapter = embededVkPlaylistsAdapter,
            context = requireContext()
        )


        binding.trendingRcv.adapter = tracksAdapter
        (binding.trendingRcv.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        binding.trendingRcv.adapter =
            ConcatAdapter(
                mainTitleAdapter,
                playlistSectionAdapter,
                secondPlaylistSectionAdapter,
                recommendationsTitleAdapter,
                tracksAdapter
            )
        binding.scrollUpButton.setupWithRecycler(binding.trendingRcv)




        lifecycleScope.launch {
            viewModel.collectState(this@TrendingFragment){
                    it.apply(binding, binding.pullToRefresh)
            }
        }


        lifecycleScope.launch{
            viewModel.collectPlaylists(this@TrendingFragment){
                playlistsAdapter.map(it)
            }
        }

        lifecycleScope.launch{
            viewModel.collectEmbeddedPlaylists(this@TrendingFragment){
                embededVkPlaylistsAdapter.map(it)
            }
        }


        lifecycleScope.launch{
            viewModel.collectData(this@TrendingFragment){
                val recyclerViewState = binding.trendingRcv.layoutManager?.onSaveInstanceState()
                tracksAdapter.map(it)
                binding.trendingRcv.layoutManager?.onRestoreInstanceState(recyclerViewState)
                viewModel.saveCurrentPageQueue(it)
            }
        }

        lifecycleScope.launch{
            viewModel.collectSelectedTrack(this@TrendingFragment){
                tracksAdapter.newPosition(it)
            }
        }

        binding.pullToRefresh.setOnRefreshListener{
            viewModel.loadData()
        }

    }
    }

