package com.kamancho.melisma.creteplaylist.presentation

import android.view.View
import androidx.navigation.NavController
import com.kamancho.melisma.databinding.PlaylistDataFragmentBinding

/**
 * Created by HP on 16.07.2023.
 **/
sealed interface PlaylistDataUiState{

    fun apply(
        binding: PlaylistDataFragmentBinding,
        navController: NavController
    )

    object Success: PlaylistDataUiState{

        override fun apply(
            binding: PlaylistDataFragmentBinding,
            navController: NavController
        ) = with(binding){
            progress.visibility = View.GONE
            error.errorTv.visibility = View.GONE
            error.errorImg.visibility = View.GONE
            error.reloadBtn.visibility = View.GONE

            titleEditText.visibility = View.VISIBLE
            titleLabel.visibility = View.VISIBLE
            descriptionEditText.visibility = View.VISIBLE
            descriptionLabel.visibility = View.VISIBLE
            selectedTracksRcv.visibility = View.VISIBLE
            addTracksBtn.visibility = View.VISIBLE

            titleEditText.isEnabled = true
            descriptionEditText.isEnabled = true
            addTracksBtn.isEnabled = true

        }

    }

    data class Error(
        private val message: String
    ): PlaylistDataUiState {


        override fun apply(
            binding: PlaylistDataFragmentBinding,
            navController: NavController
        ) = with(binding){
            progress.visibility = View.GONE
            error.errorTv.visibility = View.VISIBLE
            error.errorTv.text = message
            error.errorImg.visibility = View.VISIBLE
            error.reloadBtn.visibility = View.VISIBLE

            titleEditText.visibility = View.GONE
            titleLabel.visibility = View.GONE
            descriptionEditText.visibility = View.GONE
            descriptionLabel.visibility = View.GONE
            selectedTracksRcv.visibility = View.GONE
            addTracksBtn.visibility = View.GONE

            titleEditText.isEnabled = true
            descriptionEditText.isEnabled = true
            addTracksBtn.isEnabled = true
        }


    }



    object Loading: PlaylistDataUiState{

        override fun apply(
            binding: PlaylistDataFragmentBinding,
            navController: NavController
        ) = with(binding){
            error.errorTv.visibility = View.GONE
            error.errorImg.visibility = View.GONE
            error.reloadBtn.visibility = View.GONE

            titleEditText.visibility = View.GONE
            titleLabel.visibility = View.GONE
            descriptionEditText.visibility = View.GONE
            descriptionLabel.visibility = View.GONE
            selectedTracksRcv.visibility = View.GONE
            addTracksBtn.visibility = View.GONE

            titleEditText.isEnabled = false
            descriptionEditText.isEnabled = false
            addTracksBtn.isEnabled = false
            progress.visibility = View.VISIBLE
        }

    }

    object DisableLoading: PlaylistDataUiState{
        override fun apply(binding: PlaylistDataFragmentBinding, navController: NavController)
        = with(binding){
            progress.visibility = View.GONE

            titleEditText.visibility = View.VISIBLE
            titleLabel.visibility = View.VISIBLE
            descriptionEditText.visibility = View.VISIBLE
            descriptionLabel.visibility = View.VISIBLE
            selectedTracksRcv.visibility = View.VISIBLE
            addTracksBtn.visibility = View.VISIBLE

            titleEditText.isEnabled = true
            descriptionEditText.isEnabled = true
            addTracksBtn.isEnabled = true
        }

    }

    object PopFragment: PlaylistDataUiState {
        override fun apply(
            binding: PlaylistDataFragmentBinding,
            navController: NavController,
        ) {
            navController.popBackStack()
        }

    }
}