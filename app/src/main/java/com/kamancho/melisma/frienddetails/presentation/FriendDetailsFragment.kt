package com.kamancho.melisma.frienddetails.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.databinding.SearchHistoryFragmentBinding
import com.kamancho.melisma.frienddetails.di.FriendDetailsComponent
import com.kamancho.melisma.main.di.App
import com.kamancho.melisma.searchhistory.presentation.ViewPagerFragmentsAdapter
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

    private val args: FriendDetailsFragmentArgs by navArgs()

    private lateinit var adapter: ViewPagerFragmentsAdapter

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
            FriendTracksFragment.newInstance(args.friendId),
            FriendPlaylistsFragment.newInstance(args.friendId)
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


        binding.historyTitle.text = args.friendName
        binding.clearHistoryBtn.visibility = View.GONE
        binding.backBtnSearch.visibility = View.VISIBLE

        adapter = ViewPagerFragmentsAdapter(
            childFragmentManager,
            lifecycle,
            fragments
        )

        binding.searchHistoryViewPager.adapter = adapter

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