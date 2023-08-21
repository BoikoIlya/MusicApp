package com.example.musicapp.searchhistory.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicapp.R
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.Selector
import com.example.musicapp.databinding.SearchHistoryFragmentBinding
import com.example.musicapp.databinding.SearchHistoryListBinding
import com.example.musicapp.main.di.App
import com.example.musicapp.searchhistory.di.SearchHistoryComponent
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 13.08.2023.
 **/
abstract class SearchHistoryListFragment: Fragment(R.layout.search_history_list) {

    private val binding by viewBinding(SearchHistoryListBinding::bind)

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    protected lateinit var viewModel: SearchHistoryListViewModel

    protected lateinit var searchHistoryComponent: SearchHistoryComponent


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SearchHistoryAdapter(object : ClickListener<String> {
            override fun onClick(data: String) {
                viewModel.removeHistoryItem(data)
                viewModel.singleState(SearchHistorySingleState.ClearEditTextIfTextsMatches(data))
            }
        },object: Selector<String> {
            override fun onSelect(data: String, position: Int) {
                viewModel.singleState(SearchHistorySingleState.CheckQueryBeforeNavigation(data))
            }
        })

        binding.searchRcv.layoutManager = LinearLayoutManager(requireContext())
        (binding.searchRcv.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        binding.searchRcv.adapter = adapter

        lifecycleScope.launch{
            viewModel.collectSearchHistory(this@SearchHistoryListFragment){
                binding.message.isVisible = it.isEmpty()
                val recyclerViewState = binding.searchRcv.layoutManager?.onSaveInstanceState()
                adapter.map(it)
                binding.searchRcv.layoutManager?.onRestoreInstanceState(recyclerViewState)
            }
        }

        lifecycleScope.launch{
            viewModel.collectQueryCommunication(this@SearchHistoryListFragment){
                viewModel.fetchHistory(it)
            }
        }

    }


    class BaseSearchHistoryTracksListFragment: SearchHistoryListFragment(){

        override fun onAttach(context: Context) {
            super.onAttach(context)
            searchHistoryComponent = (context.applicationContext as App).appComponent.searchHistoryComponent().build()
            searchHistoryComponent.inject(this)
            viewModel =
                ViewModelProvider(this, factory)[BaseSearchHistoryTracksListViewModel::class.java]
        }
    }

    class BaseSearchHistoryPlaylistsListFragment: SearchHistoryListFragment(){

        override fun onAttach(context: Context) {
            super.onAttach(context)
            searchHistoryComponent = (context.applicationContext as App).appComponent.searchHistoryComponent().build()
            searchHistoryComponent.inject(this)
            viewModel =
                ViewModelProvider(this, factory)[BaseSearchHistoryPlaylistsListViewModel::class.java]
        }
    }
}