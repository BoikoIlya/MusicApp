package com.kamancho.melisma.search.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.databinding.SearchHistoryListBinding
import com.kamancho.melisma.searchhistory.di.SearchHistoryComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 14.08.2023.
 **/

abstract class SearchListFragment<T:Any,V:ViewHolder>: Fragment(R.layout.search_history_list) {

    protected val binding by viewBinding(SearchHistoryListBinding::bind)

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    protected lateinit var viewModel:SearchListViewModel<T>

    @Inject
    lateinit var imageLoader: ImageLoader


    protected lateinit var adapter: PagingDataAdapter<T, V>

    protected lateinit var searchHistoryComponent: SearchHistoryComponent

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchLoadStateBar.root.visibility = View.VISIBLE

        val onRetryCallBack = object : OnRetry{
            override fun onRetry() {
                adapter.retry()
            }
        }

        val defaultLoadStateAdapter = DefaultLoadStateAdapter(onRetryCallBack,viewModel::readErrorMessage)

        binding.searchRcv.adapter = adapter.withLoadStateFooter(defaultLoadStateAdapter)
        (binding.searchRcv.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false


        lifecycleScope.launch {
            viewModel.list.collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launch{
            adapter.loadStateFlow.collect{
                val errorState= when{
                    it.append is LoadState.Error -> it.append as LoadState.Error
                    it.refresh is LoadState.Error -> it.refresh as LoadState.Error
                    it.prepend is LoadState.Error -> it.prepend as LoadState.Error
                    else -> null
                }
                viewModel.saveErrorMessage(errorState?.error?.message.toString())
                LoadStateViewHolder(binding.searchLoadStateBar,onRetryCallBack, defaultLoadStateAdapter)
                    .bind(it.refresh,{errorState?.error?.message.toString()}, errorState?.error?.cause as Exception?)
            }
        }
    }



}