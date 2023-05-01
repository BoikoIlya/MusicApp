package com.example.musicapp.search.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.R
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.main.di.App
import com.example.musicapp.queue.di.QueueComponent
import com.example.musicapp.queue.presenatation.QueueViewModel
import com.example.musicapp.search.di.SearchComponent
import javax.inject.Inject

/**
 * Created by HP on 26.01.2023.
 **/
class SearchFragment: Fragment(R.layout.search_fragment) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: SearchViewModel


    private lateinit var playerComponent: SearchComponent

    override fun onAttach(context: Context) {
        super.onAttach(context)
        playerComponent = (context.applicationContext as App).appComponent.searchComponent().build()
        playerComponent.inject(this)
        viewModel =  ViewModelProvider(this, factory)[SearchViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}