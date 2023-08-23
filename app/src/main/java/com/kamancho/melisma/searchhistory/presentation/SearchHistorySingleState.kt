package com.kamancho.melisma.searchhistory.presentation

import androidx.navigation.NavController
import com.kamancho.melisma.R
import com.kamancho.melisma.databinding.SearchHistoryFragmentBinding

/**
 * Created by HP on 30.06.2023.
 **/
sealed interface SearchHistorySingleState {

    fun apply(
        navController: NavController,
        viewModel: SearchHistoryViewModel,
        binding: SearchHistoryFragmentBinding
    )

    object NavigateToSearch: SearchHistorySingleState {
        override fun apply(
            navController: NavController,
            viewModel: SearchHistoryViewModel,
            binding: SearchHistoryFragmentBinding
        ) {
            navController.navigate(R.id.action_searchHistoryFragment_to_searchFragment)
        }
    }



    data class CheckQueryBeforeNavigation(
        private val query: String
    ) : SearchHistorySingleState {

        override fun apply(
            navController: NavController,
            viewModel: SearchHistoryViewModel,
            binding: SearchHistoryFragmentBinding,
        ) {
            binding.searchHistoryEdt.setText(query)
            viewModel.checkQueryBeforeNavigation(query)
        }

    }

    data class ClearEditTextIfTextsMatches(
        private val text: String
    ) : SearchHistorySingleState {


        override fun apply(
            navController: NavController,
            viewModel: SearchHistoryViewModel,
            binding: SearchHistoryFragmentBinding,
        ) {
            if (binding.searchHistoryEdt.text.toString() == text)
                binding.searchHistoryEdt.setText("")
        }
    }
}