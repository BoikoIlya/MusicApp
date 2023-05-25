package com.example.musicapp.trending.presentation

import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.media3.common.MediaItem
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.ErrorLayoutBinding
import com.example.musicapp.databinding.FragmentPlaylistBinding
import com.example.musicapp.musicdialog.presentation.MusicDialogFragment

sealed interface TracksUiState {

    fun apply(
        errorLayoutBinding: ErrorLayoutBinding,
        progress: ProgressBar,
    )

    fun apply(
        binding: FragmentPlaylistBinding
    )

    object Success: TracksUiState{

        override fun apply(
            errorLayoutBinding: ErrorLayoutBinding,
            progress: ProgressBar,
        ) {
            progress.visibility = View.GONE
            errorLayoutBinding.root.visibility = View.GONE
        }

        override fun apply(binding: FragmentPlaylistBinding) = with(binding) {
            playlistProgress.visibility = View.GONE
            errorLayout.root.visibility = View.GONE
            playlistImgCard.visibility = View.VISIBLE
            trackAmount.visibility = View.VISIBLE
            description.visibility = View.VISIBLE
            albumName .visibility = View.VISIBLE
            shuffleBtn.visibility = View.VISIBLE
        }
    }

    data class Error(
        private val message: String
    ): TracksUiState{

        override fun apply(
            errorLayoutBinding: ErrorLayoutBinding,
            progress: ProgressBar,
        ) {
            progress.visibility = View.GONE
            errorLayoutBinding.errorTv.text = message
            errorLayoutBinding.root.visibility = View.VISIBLE
        }

        override fun apply(binding: FragmentPlaylistBinding) = with(binding) {
            playlistImgCard.visibility = View.GONE
            trackAmount.visibility = View.GONE
            description.visibility = View.GONE
            albumName .visibility = View.GONE
            shuffleBtn.visibility = View.GONE
            playlistProgress.visibility = View.GONE
            errorLayout.errorTv.text = message
            errorLayout.root.visibility = View.VISIBLE
        }
    }

    object Loading: TracksUiState{

        override fun apply(
            errorLayoutBinding: ErrorLayoutBinding,
            progress: ProgressBar,
        ) {
            errorLayoutBinding.root.visibility = View.GONE
            progress.visibility = View.VISIBLE
        }

        override fun apply(binding: FragmentPlaylistBinding) = with(binding) {
            playlistImgCard.visibility = View.GONE
            trackAmount.visibility = View.GONE
            description.visibility = View.GONE
            albumName .visibility = View.GONE
            shuffleBtn.visibility = View.GONE
            errorLayout.root.visibility = View.GONE
            playlistProgress.visibility = View.VISIBLE

        }

    }




}
