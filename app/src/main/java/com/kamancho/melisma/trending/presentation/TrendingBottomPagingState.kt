package com.kamancho.melisma.trending.presentation


import android.util.Log
import com.kamancho.melisma.app.core.PagingListener
import com.kamancho.melisma.databinding.TrendingFragmentBinding
import com.simform.refresh.SSPullToRefreshLayout

sealed interface TrendingBottomPagingState {

    fun apply(
        pagingListener: PagingListener,
        loadStateAdapter: PagingLoadStateAdapter
    )


    object LoadingBottom: TrendingBottomPagingState{

        override fun apply(
            pagingListener: PagingListener,
            loadStateAdapter: PagingLoadStateAdapter
        ) {
            pagingListener.setLoading(true)
            loadStateAdapter.show("")
        }

    }

    data class Error(
        private val message: String
    ): TrendingBottomPagingState{

        override fun apply(
            pagingListener: PagingListener,
            loadStateAdapter: PagingLoadStateAdapter
        ) {
            Log.d("tag", "apply: $message ")
            loadStateAdapter.show(message)
            pagingListener.setLoading(false)
        }

    }

    object DisableLoadingBottom: TrendingBottomPagingState{

        override fun apply(
            pagingListener: PagingListener,
            loadStateAdapter: PagingLoadStateAdapter
        ) {
            loadStateAdapter.hide()
            pagingListener.setLoading(false)
        }
    }


}
