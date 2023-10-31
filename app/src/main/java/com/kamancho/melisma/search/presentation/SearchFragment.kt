package com.kamancho.melisma.search.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kamancho.melisma.R
import com.kamancho.melisma.databinding.SearchHistoryFragmentBinding
import com.kamancho.melisma.main.di.App
import com.kamancho.melisma.search.di.SearchComponent
import com.kamancho.melisma.searchhistory.presentation.ViewPagerFragmentsAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.kamancho.melisma.searchhistory.presentation.SearchHistoryFragment.Companion.search_request_key
import com.kamancho.melisma.searchhistory.presentation.SearchHistorySingleState
import com.kamancho.melisma.searchhistory.presentation.SearchHistorySingleState.NavigateToSearch.Companion.search_type_arg_key
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * Created by HP on 26.01.2023.
 **/
@ExperimentalCoroutinesApi
 class SearchFragment: Fragment(R.layout.search_history_fragment) {

    protected val binding by viewBinding(SearchHistoryFragmentBinding::bind)

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: SearchViewModel

    private val args: SearchFragmentArgs by navArgs()

    private lateinit var searchComponent: SearchComponent

    private lateinit var tabTitlesList: List<String>
    private lateinit var fragments: List<Fragment>

    private var viewPagerIndex: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        searchComponent = (context.applicationContext as App).appComponent.searchComponent().build()
        searchComponent.inject(this)
        viewModel =  ViewModelProvider(this, factory)[SearchViewModel::class.java]

        tabTitlesList = listOf(
            requireContext().getString(R.string.tab_tracks_title),
            requireContext().getString(R.string.tab_playlists_title)
        )

        fragments = listOf(
            BaseSearchTracksFragment.newInstance(args.searchQuery),
            BaseSearchPlaylistsFragment.newInstance(args.searchQuery)
        )

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.historyTitle.visibility = View.GONE
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




        viewPagerIndex = args.viewPagerIndex
        binding.searchHistoryEdt.setText(args.searchQuery)
        binding.searchHistoryViewPager.setCurrentItem(viewPagerIndex,false)


        binding.searchHistoryEdt.setOnFocusChangeListener { _, isFocused ->
            if (isFocused){
                setFragmentResult(
                    search_request_key,
                    bundleOf(Pair(search_type_arg_key,viewPagerIndex))
                )
                findNavController().popBackStack()
            }
        }

        binding.searchHistoryViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewPagerIndex = position

            }
        })


        binding.backBtnSearch.setOnClickListener {
            setFragmentResult(
                search_request_key,
                bundleOf(Pair(search_type_arg_key,viewPagerIndex))
            )
            findNavController().popBackStack()
        }

    }



}