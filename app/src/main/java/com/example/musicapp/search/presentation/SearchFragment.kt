package com.example.musicapp.search.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicapp.R
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.app.core.Selector
import com.example.musicapp.databinding.SearchFragmentBinding
import com.example.musicapp.main.di.App
import com.example.musicapp.search.di.SearchComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 26.01.2023.
 **/
@OptIn(FlowPreview::class)
@ExperimentalCoroutinesApi
class SearchFragment: Fragment(R.layout.search_fragment) {

    private val binding by viewBinding(SearchFragmentBinding::bind)

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var viewModel: SearchViewModel

    private lateinit var searchComponent: SearchComponent

    private lateinit var textWatcher: TextWatcher

    override fun onAttach(context: Context) {
        super.onAttach(context)
        searchComponent = (context.applicationContext as App).appComponent.searchComponent().build()
        searchComponent.inject(this)
        viewModel =  ViewModelProvider(this, factory)[SearchViewModel::class.java]

        textWatcher = object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.saveSearchTerm(text.toString())
            }

            override fun afterTextChanged(p0: Editable?) = Unit
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchRcv.layoutManager =
            LinearLayoutManager(requireContext())

        val tracksAdapter = SearchPagingAdapter(  this.requireContext(),
            playClickListener = object: Selector<MediaItem> {
                override fun onSelect(data: MediaItem, position: Int) {
                    viewModel.playMusic(data)
                }
            }, saveClickListener = object : ClickListener<MediaItem> {
                override fun onClick(data: MediaItem) {
                    viewModel.addTrackToFavorites(data)
                }
            }, imageLoader)



        val onRetryCallBack = object : OnRetry{
            override fun onRetry() {
                tracksAdapter.retry()
            }
        }

        val defaultLoadStateAdapter = DefaultLoadStateAdapter(onRetryCallBack,viewModel::readErrorMessage)

        binding.searchRcv.adapter = tracksAdapter.withLoadStateFooter(defaultLoadStateAdapter)

        val data = viewModel.readQuery()
        binding.searchEdt.setText(data)
        viewModel.findTracks()
        viewModel.saveCurrentPageQueue(emptyList())


        binding.searchEdt.setOnFocusChangeListener { _, isFocused ->
            if (isFocused) findNavController().popBackStack()
        }

        lifecycleScope.launch {
            viewModel.tracks.collectLatest {
                tracksAdapter.submitData(it)

            }
        }

        lifecycleScope.launch{
            viewModel.collectSelectedTrack(this@SearchFragment){
                tracksAdapter.newPosition(it)
            }
        }

        lifecycleScope.launch {
            viewModel.collectPlayerControls(this@SearchFragment){
                it.apply(binding.searchRcv)
            }
        }


        lifecycleScope.launch{
            tracksAdapter.loadStateFlow.collect{
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

        binding.searchEdt.setOnKeyListener { view, keyCode, keyEvent ->
           return@setOnKeyListener if(keyCode==KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP){
               lifecycleScope.launch { tracksAdapter.submitData(PagingData.empty()) }
               viewModel.findTracks()
               true
           }else false
        }


    }

    override fun onStart() {
        binding.searchEdt.addTextChangedListener(textWatcher)
        super.onStart()
    }


    override fun onStop() {
        binding.searchEdt.removeTextChangedListener(textWatcher)
        super.onStop()
    }

}