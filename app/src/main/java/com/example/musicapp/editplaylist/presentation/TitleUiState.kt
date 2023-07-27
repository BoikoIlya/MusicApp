package com.example.musicapp.editplaylist.presentation

import com.example.musicapp.databinding.PlaylistDataFragmentBinding

/**
 * Created by HP on 19.07.2023.
 **/
sealed interface TitleUiState {

    fun apply(binding: PlaylistDataFragmentBinding)

    object Success: TitleUiState {
        override fun apply(binding: PlaylistDataFragmentBinding) {
            binding.titleInputLayout.isErrorEnabled = false
            binding.titleInputLayout.error = ""
        }
    }

    data class Error(
        private val message: String
    ): TitleUiState {
        override fun apply(binding: PlaylistDataFragmentBinding) {
            binding.titleInputLayout.error = message
            binding.titleInputLayout.isErrorEnabled = true
        }
    }
}