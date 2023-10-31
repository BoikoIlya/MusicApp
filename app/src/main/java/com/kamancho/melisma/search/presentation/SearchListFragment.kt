package com.kamancho.melisma.search.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.app.core.LoadStateAdapter
import com.kamancho.melisma.app.core.Mapper
import com.kamancho.melisma.app.core.PagingListener
import com.kamancho.melisma.app.core.PagingSource
import com.kamancho.melisma.databinding.SearchHistoryListBinding
import com.kamancho.melisma.searchhistory.di.SearchHistoryComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 14.08.2023.
 **/

abstract class SearchListFragment<T:Any,V:ViewHolder,L,N>: Fragment(R.layout.search_history_list) {

    protected val binding by viewBinding(SearchHistoryListBinding::bind)

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    protected lateinit var viewModel:SearchListViewModel<N,L,T>

    @Inject
    lateinit var imageLoader: ImageLoader


    protected lateinit var adapter: Mapper<List<T>,Unit>

    protected lateinit var concatAdapter: ConcatAdapter

    protected lateinit var defaultLoadStateAdapter: LoadStateAdapter

    protected lateinit var searchHistoryComponent: SearchHistoryComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.loadPage(getQuery(),savedInstanceState==null)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        defaultLoadStateAdapter = LoadStateAdapter{
            viewModel.loadPage(getQuery(),true)
        }

        concatAdapter = ConcatAdapter(adapter as RecyclerView.Adapter<out ViewHolder> ,defaultLoadStateAdapter)

        val pagingListener = PagingListener.Base(PagingSource.TracksPagingSource.pageSize,4){
            viewModel.loadPage(getQuery(),true)
        }

        binding.searchRcv.adapter = concatAdapter
        binding.searchRcv.addOnScrollListener(pagingListener)
        (binding.searchRcv.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

        lifecycleScope.launch {
            viewModel.collectLoadStateCommunication(this@SearchListFragment){
                it.apply(defaultLoadStateAdapter,pagingListener)
            }
        }

        lifecycleScope.launch {
            viewModel.collectDataListCommunication(this@SearchListFragment){
                Log.d("tag", "onViewCreated: ${it.size} ")
                adapter.map(it)
                onRecyclerNewList(it)
            }
        }


    }

    protected abstract fun getQuery(): String

    protected open fun onRecyclerNewList(data: List<T>){}
}