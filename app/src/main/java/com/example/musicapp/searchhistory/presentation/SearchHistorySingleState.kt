package com.example.musicapp.searchhistory.presentation

import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.SearchHistoryFragmentBinding

/**
 * Created by HP on 30.06.2023.
 **/
sealed interface SearchHistorySingleState{

    fun apply(
        navController: NavController,
        viewModel: SearchHistoryViewModel,
        binding: SearchHistoryFragmentBinding
    )

    object NavigateToSearchFragment: SearchHistorySingleState{
        override fun apply(
            navController: NavController,
            viewModel: SearchHistoryViewModel,
            binding: SearchHistoryFragmentBinding
        ) {
            navController.navigate(R.id.action_searchHistoryFragment_to_searchFragment)
        }
    }


}