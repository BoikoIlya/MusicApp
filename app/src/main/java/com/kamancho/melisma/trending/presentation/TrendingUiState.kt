package com.kamancho.melisma.trending.presentation


import com.kamancho.melisma.app.core.PagingListener
import com.kamancho.melisma.databinding.TrendingFragmentBinding
import com.simform.refresh.SSPullToRefreshLayout

sealed interface TrendingUiState {

    fun apply(
        binding: TrendingFragmentBinding,
        progress: SSPullToRefreshLayout,
    )

    object DisableLoading: TrendingUiState{

        override fun apply(
            binding: TrendingFragmentBinding,
            progress: SSPullToRefreshLayout,
        ) {
            progress.setRefreshing(false)
            progress.isEnabled = false
            progress.isEnabled = true
        }
    }




    object Loading: TrendingUiState{

        override fun apply(
            binding: TrendingFragmentBinding,
            progress: SSPullToRefreshLayout,
        ) {

            progress.setRefreshing(true)
        }

    }


}
