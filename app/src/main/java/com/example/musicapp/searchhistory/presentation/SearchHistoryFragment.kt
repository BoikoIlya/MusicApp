package com.example.musicapp.searchhistory.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicapp.R
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.Selector
import com.example.musicapp.databinding.SearchHistoryFragmentBinding
import com.example.musicapp.main.di.App
import com.example.musicapp.searchhistory.di.SearchHistoryComponent
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        searchHistoryComponent = (context.applicationContext as App).appComponent.searchHistoryComponent().build()
        searchHistoryComponent.inject(this)
        viewModel =  ViewModelProvider(this, factory)[SearchHistoryViewModel::class.java]

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.fetchHistory(text.toString())
            }

            override fun afterTextChanged(p0: Editable?) = Unit
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SearchHistoryAdapter(object : ClickListener<String>{
            override fun onClick(data: String) {
                viewModel.removeHistoryItem(data)
                binding.searchHistoryEdt.setText("")
            }
        },object: Selector<String>{
            override fun onSelect(data: String, position: Int) {
                viewModel.checkQueryBeforeNavigation(data)
            }
        })

        binding.searchHistoryRcv.layoutManager = LinearLayoutManager(requireContext())
        (binding.searchHistoryRcv.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

        binding.searchHistoryEdt.setOnKeyListener { _, keyCode, keyEvent ->
            return@setOnKeyListener if(keyCode== KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP){
                viewModel.checkQueryBeforeNavigation(binding.searchHistoryEdt.text.toString())

                true
            }else false
        }


        binding.searchHistoryRcv.adapter = adapter

        lifecycleScope.launch{
            viewModel.collectSearchHistory(this@SearchHistoryFragment){
                binding.searchHistoryMessage.isVisible = it.isEmpty()
                adapter.map(it)
                binding.searchHistoryRcv.scrollToPosition(0)
            }
        }

        lifecycleScope.launch{
            viewModel.collectSearchQuery(this@SearchHistoryFragment){
                binding.searchHistoryEdt.setText(it)
            }
        }

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

        lifecycleScope.launch {
            viewModel.collectPlayerControls(this@SearchHistoryFragment){
                it.apply(binding.searchHistoryRcv)
            }
        }

        binding.clearHistoryBtn.setOnClickListener {
            viewModel.launchClearHistoryDialog()
        }

    }




    override fun onStart() {
        binding.searchHistoryEdt.requestFocus()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.searchHistoryEdt, InputMethodManager.SHOW_IMPLICIT)
        binding.searchHistoryEdt.addTextChangedListener(textWatcher)
        super.onStart()
    }


    override fun onPause() {
        super.onPause()
        viewModel.saveQueryToCommuniction(binding.searchHistoryEdt.text.toString())
    }



    override fun onStop() {
        binding.searchHistoryEdt.removeTextChangedListener(textWatcher)
        super.onStop()
    }
}