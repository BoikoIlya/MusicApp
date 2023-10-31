package com.kamancho.melisma.searchhistory.presentation

import android.os.Bundle
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

    data class NavigateToSearch(
        private val query: String,
        private val historyType: Int
    ): SearchHistorySingleState {

        companion object{
             const val search_query_arg_key = "searchQuery"
             const val search_type_arg_key = "viewPagerIndex"
        }

        override fun apply(
            navController: NavController,
            viewModel: SearchHistoryViewModel,
            binding: SearchHistoryFragmentBinding
        ) {
            val bundle = Bundle()
            bundle.putString(search_query_arg_key,query)
            bundle.putInt(search_type_arg_key,historyType)
            navController.navigate(R.id.action_searchHistoryFragment_to_searchFragment,bundle)
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