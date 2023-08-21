package com.example.musicapp.frienddetails.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicapp.R
import com.example.musicapp.app.core.FavoritesFragment
import com.example.musicapp.app.core.FavoritesViewModel
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.app.core.Mapper
import com.example.musicapp.databinding.FriendDetailsListBinding
import com.example.musicapp.favorites.presentation.FavoritesUiState
import com.example.musicapp.frienddetails.di.FriendDetailsComponent
import com.example.musicapp.userplaylists.data.cache.PlaylistDao
import com.simform.refresh.SSPullToRefreshLayout
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 18.08.2023.
 **/
abstract class FriendDataFragment<T>: Fragment(R.layout.friend_details_list) {

    protected val binding by viewBinding(FriendDetailsListBinding::bind)

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    protected lateinit var viewModel: FavoritesViewModel<FavoritesUiState,T>

    @Inject
    lateinit var imageLoader: ImageLoader

    protected lateinit var adapter: Mapper<List<T>,Unit>

    protected lateinit var component: FriendDetailsComponent



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pullToRefresh.setRepeatCount(SSPullToRefreshLayout.RepeatCount.INFINITE)
        binding.pullToRefresh.setLottieAnimation(FavoritesFragment.loading_animation)
        binding.pullToRefresh.setRefreshViewParams(
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                250,
            ))


        (binding.friendDetailsRcv.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

        lifecycleScope.launch{
            viewModel.collectLoading(this@FriendDataFragment){
                it.apply(
                    binding.message,
                    binding.pullToRefresh,
                    binding.friendDetailsRcv
                )
            }
        }

        lifecycleScope.launch{
            viewModel.collectState(this@FriendDataFragment){
                Log.d("tag", "onViewCreated: $it")
                it.apply(
                    binding.message,
                    binding.pullToRefresh,
                    binding.friendDetailsRcv
                )
            }
        }

        lifecycleScope.launch{
            viewModel.collectData(this@FriendDataFragment){
                Log.d("tag", "onViewCreated: ${it.size}")
                adapter.map(it)
                onRecyclerDataUpdate(it)
            }
        }

        lifecycleScope.launch{
            (viewModel as FriendsDataViewModel).collectSearchQuery(this@FriendDataFragment){
                (viewModel as FriendsDataViewModel).search(it)
            }
        }

        binding.pullToRefresh.setOnRefreshListener{
            viewModel.update(true)
        }

    }

    protected abstract fun onRecyclerDataUpdate(data: List<T>)
}