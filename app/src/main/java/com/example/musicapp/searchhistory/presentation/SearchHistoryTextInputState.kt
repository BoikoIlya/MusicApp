package com.example.musicapp.searchhistory.presentation

import com.example.musicapp.databinding.SearchHistoryFragmentBinding

/**
 * Created by HP on 30.06.2023.
 **/
sealed interface SearchHistoryTextInputState{

    fun apply(
        binding: SearchHistoryFragmentBinding
    )

    data class ShowError(
        private val message: String
    ): SearchHistoryTextInputState{

        override fun apply(binding: SearchHistoryFragmentBinding) {
            binding.textInputLayoutSearchHistory.error = message
        }

    }

    object ResetError: SearchHistoryTextInputState{
        override fun apply(binding: SearchHistoryFragmentBinding) {
            binding.textInputLayoutSearchHistory.isErrorEnabled = false
            binding.textInputLayoutSearchHistory.error = ""
        }
    }

    object Nothing: SearchHistoryTextInputState{
        override fun apply(binding: SearchHistoryFragmentBinding) = Unit
    }
}