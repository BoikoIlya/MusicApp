package com.example.musicapp.frienddetails.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicapp.R
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.databinding.SearchHistoryFragmentBinding
import com.example.musicapp.databinding.SearchHistoryListBinding
import com.example.musicapp.frienddetails.di.FriendDetailsComponent
import com.example.musicapp.main.di.App
import com.example.musicapp.search.presentation.BaseSearchPlaylistsFragment
import com.example.musicapp.search.presentation.BaseSearchTracksFragment
import com.example.musicapp.search.presentation.SearchListViewModel
import com.example.musicapp.searchhistory.presentation.ViewPagerFragmentsAdapter
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

/**
 * Created by HP on 18.08.2023.
 **/
class FriendDetailsFragment: Fragment(R.layout.search_history_fragment) {

    private val binding by viewBinding(SearchHistoryFragmentBinding::bind)

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: FriendDetailsViewModel

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var component: FriendDetailsComponent

    private lateinit var tabTitlesList: List<String>
    private lateinit var fragments: List<Fragment>
    private lateinit var textWatcher: TextWatcher

    override fun onAttach(context: Context) {
        super.onAttach(context)

        component = (context.applicationContext as App).appComponent.friendDetailsComponent().build()
        component.inject(this)
        viewModel = ViewModelProvider(this, factory)[FriendDetailsViewModel::class.java]

        tabTitlesList = listOf(
            requireContext().getString(R.string.tab_tracks_title),
            requireContext().getString(R.string.tab_playlists_title)
        )

        fragments = listOf(
            FriendTracksFragment(),
            FriendPlaylistsFragment()
        )

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.saveQueryToCommunication(text.toString())
            }

            override fun afterTextChanged(p0: Editable?) = Unit
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.historyTitle.text = viewModel.readFirstAndSecondName()
        binding.clearHistoryBtn.visibility = View.GONE
        binding.backBtnSearch.visibility = View.VISIBLE



        binding.searchHistoryViewPager.adapter = ViewPagerFragmentsAdapter(
            childFragmentManager,
            lifecycle,
            fragments
        )

        TabLayoutMediator(binding.tabLayout,binding.searchHistoryViewPager){tab,position->
            tab.text = tabTitlesList[position]
        }.attach()


        binding.backBtnSearch.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    override fun onStart() {
        binding.searchHistoryEdt.addTextChangedListener(textWatcher)
        super.onStart()
    }


    override fun onStop() {
        binding.searchHistoryEdt.removeTextChangedListener(textWatcher)
        super.onStop()
    }
}