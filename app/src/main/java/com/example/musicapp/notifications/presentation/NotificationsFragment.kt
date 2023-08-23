package com.example.musicapp.notifications.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicapp.R
import com.example.musicapp.app.core.FavoritesFragment
import com.example.musicapp.databinding.NotificationsFragmentBinding
import com.example.musicapp.databinding.TrendingFragmentBinding
import com.example.musicapp.main.di.App
import com.example.musicapp.main.di.AppComponent
import com.simform.refresh.SSPullToRefreshLayout
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 21.08.2023.
 **/
class NotificationsFragment: Fragment(R.layout.notifications_fragment) {

    private val binding by viewBinding(NotificationsFragmentBinding::bind)

    private lateinit var viewModel: NotificationsViewModel

    private lateinit var component: AppComponent

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component = (context.applicationContext as App).appComponent
        component.inject(this)

        viewModel = ViewModelProvider(this, factory)[NotificationsViewModel::class.java]
        viewModel.hideNotificationsBadge()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pullToRefresh.setRepeatCount(SSPullToRefreshLayout.RepeatCount.INFINITE)
        binding.pullToRefresh.setLottieAnimation(FavoritesFragment.loading_animation)
        binding.pullToRefresh.setRefreshViewParams(
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                250))

        binding.notificationsRcv.layoutManager = LinearLayoutManager(requireContext())
        binding.scrollUpButton.setupWithRecycler(binding.notificationsRcv)

        val adapter = NotificationsAdapter(requireContext())
        binding.notificationsRcv.adapter = adapter
        (binding.notificationsRcv.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

        lifecycleScope.launch {
            viewModel.collectNotifications(this@NotificationsFragment){
                val state = binding.notificationsRcv.layoutManager?.onSaveInstanceState()
                adapter.map(it)
                binding.notificationsRcv.layoutManager?.onRestoreInstanceState(state)
            }
        }

        lifecycleScope.launch {
            viewModel.collectUiState(this@NotificationsFragment){
                it.apply(
                    binding.noNotificationsMessage,
                    binding.notificationImg,
                    binding.pullToRefresh,
                    binding.notificationsRcv
                )
            }
        }

        binding.pullToRefresh.setOnRefreshListener{
            viewModel.update()
        }
    }
}