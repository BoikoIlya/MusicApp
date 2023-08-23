package com.example.musicapp.popular.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.musicapp.R
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.FavoritesFragment
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.app.core.Selector
import com.example.musicapp.databinding.NotificationsFragmentBinding
import com.example.musicapp.main.di.App
import com.example.musicapp.main.di.AppComponent
import com.example.musicapp.notifications.presentation.NotificationsAdapter
import com.example.musicapp.notifications.presentation.NotificationsViewModel
import com.example.musicapp.popular.di.PopularComponent
import com.example.musicapp.trending.presentation.TracksAdapter
import com.simform.refresh.SSPullToRefreshLayout
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 22.08.2023.
 **/
class PopularFragment: Fragment(R.layout.notifications_fragment) {


    private val binding by viewBinding(NotificationsFragmentBinding::bind)

    private lateinit var viewModel: PopularViewModel

    private lateinit var component: PopularComponent

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component = (context.applicationContext as App).appComponent.popularComponent().build()
        component.inject(this)

        viewModel = ViewModelProvider(this, factory)[PopularViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.titleTv.text = getString(R.string.popular)
        binding.noNotificationsMessage.text = getString(R.string.no_favorite_tracks)
        binding.backBtn.visibility = View.VISIBLE
        binding.pullToRefresh.setRepeatCount(SSPullToRefreshLayout.RepeatCount.INFINITE)
        binding.pullToRefresh.setLottieAnimation(FavoritesFragment.loading_animation)
        binding.pullToRefresh.setRefreshViewParams(
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                250))

        binding.notificationsRcv.layoutManager = LinearLayoutManager(requireContext())
        binding.scrollUpButton.setupWithRecycler(binding.notificationsRcv)

        val adapter = TracksAdapter(
            context =requireContext(),
            playClickListener = object : Selector<MediaItem>{
                override fun onSelect(data: MediaItem, position: Int) {
                    viewModel.playMusic(data)
                } },
            saveClickListener = object : ClickListener<MediaItem>{
                override fun onClick(data: MediaItem) {
                    viewModel.checkAndAddTrackToFavorites(data)
                } },
            imageLoader = imageLoader,
            addBtnVisibility = View.VISIBLE,
            layoutManager = binding.notificationsRcv.layoutManager as LayoutManager
        )

        binding.notificationsRcv.adapter = adapter
        (binding.notificationsRcv.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

        lifecycleScope.launch {
            viewModel.collectData(this@PopularFragment){
                binding.noNotificationsMessage.isVisible = it.isEmpty()
                val state = binding.notificationsRcv.layoutManager?.onSaveInstanceState()
                adapter.map(it)
                binding.notificationsRcv.layoutManager?.onRestoreInstanceState(state)
                viewModel.saveCurrentPageQueue(it)
            }
        }

        lifecycleScope.launch {
            viewModel.collectState(this@PopularFragment){
                it.apply(
                    binding.noNotificationsMessage,
                    binding.pullToRefresh,
                    binding.notificationsRcv
                )
            }
        }

        lifecycleScope.launch {
            viewModel.collectSelectedTrack(this@PopularFragment){
               adapter.newPosition(it)
            }
        }

        binding.pullToRefresh.setOnRefreshListener{
            viewModel.update(false)
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}