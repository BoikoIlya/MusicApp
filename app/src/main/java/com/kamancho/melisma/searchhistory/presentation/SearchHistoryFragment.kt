package com.kamancho.melisma.searchhistory.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kamancho.melisma.R
import com.kamancho.melisma.databinding.SearchHistoryFragmentBinding
import com.kamancho.melisma.main.di.App
import com.kamancho.melisma.searchhistory.di.SearchHistoryComponent
import com.google.android.material.tabs.TabLayoutMediator
import com.kamancho.melisma.app.core.Logger
import com.kamancho.melisma.searchhistory.presentation.SearchHistorySingleState.NavigateToSearch.Companion.search_type_arg_key
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by HP on 01.05.2023.
 **/
class SearchHistoryFragment: Fragment(R.layout.search_history_fragment) {

    private val binding by viewBinding(SearchHistoryFragmentBinding::bind)

    @Inject
    lateinit var factory: ViewModelProvider.Factory


    private lateinit var viewModel: SearchHistoryViewModel

    private lateinit var searchHistoryComponent: SearchHistoryComponent

    private lateinit var textWatcher: TextWatcher

    private lateinit var tabTitlesList: List<String>
    private lateinit var fragments: List<Fragment>


    override fun onAttach(context: Context) {
        super.onAttach(context)
        searchHistoryComponent = (context.applicationContext as App).appComponent.searchHistoryComponent().build()
        searchHistoryComponent.inject(this)
        viewModel =  ViewModelProvider(this, factory)[SearchHistoryViewModel::class.java]

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.findInHistory(text.toString())
            }

            override fun afterTextChanged(p0: Editable?) = Unit
        }
        tabTitlesList = listOf(
            requireContext().getString(R.string.tab_tracks_title),
            requireContext().getString(R.string.tab_playlists_title)
        )
        fragments = listOf(
            SearchHistoryListFragment.BaseSearchHistoryTracksListFragment(),
            SearchHistoryListFragment.BaseSearchHistoryPlaylistsListFragment()
        )
    }

    companion object{
        const val search_request_key = "search_request_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(search_request_key) { key, bundle ->
          binding.searchHistoryViewPager.setCurrentItem(bundle.getInt(search_type_arg_key),false)
        }
        Logger.logFragment(
            findNavController().currentDestination?.label.toString(),
            requireContext()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val adapter = ViewPagerFragmentsAdapter(childFragmentManager,lifecycle,fragments)
        binding.searchHistoryViewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout,binding.searchHistoryViewPager){tab,position->
            tab.text = tabTitlesList[position]
        }.attach()

        binding.searchHistoryViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.changeHistoryType(position)
            }
        })


       viewLifecycleOwner.lifecycleScope.launch{
            viewModel.collectSearchHistorySingleStateCommunication(this@SearchHistoryFragment){
                it.apply(findNavController(), viewModel, binding)
            }
        }

        lifecycleScope.launch{
            viewModel.collectSearchHistoryInputStateCommunication(this@SearchHistoryFragment){
                it.apply(binding)
            }
        }



        binding.searchHistoryEdt.setOnKeyListener { _, keyCode, keyEvent ->
            return@setOnKeyListener if(keyCode== KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP){
                viewModel.checkQueryBeforeNavigation(binding.searchHistoryEdt.text.toString())
                true
            }else false
        }

        binding.clearHistoryBtn.setOnClickListener {
            viewModel.launchClearHistoryDialog()
        }


    }




    override fun onStart() {
        super.onStart()
        binding.searchHistoryEdt.requestFocus()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.searchHistoryEdt, InputMethodManager.SHOW_IMPLICIT)
        binding.searchHistoryEdt.addTextChangedListener(textWatcher)
    }




    override fun onStop() {
        binding.searchHistoryEdt.removeTextChangedListener(textWatcher)
        super.onStop()
    }
}