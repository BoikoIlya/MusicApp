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
import com.example.musicapp.musicdialog.presentation.MusicDialogFragment

sealed class TracksUiState {

    abstract fun apply(
        imageView: ImageView,
        textView: TextView,
        imageButton: ImageButton,
        progress: ProgressBar,
    )

    object Success: TracksUiState(){

        override fun apply(
            imageView: ImageView,
            textView: TextView,
            imageButton: ImageButton,
            progress: ProgressBar,
        ) {
            progress.visibility = View.GONE
            imageView.visibility = View.GONE
            textView.visibility = View.GONE
            imageButton.visibility = View.GONE
        }
    }

    data class Error(
        private val message: String
    ): TracksUiState(){

        override fun apply(
            imageView: ImageView,
            textView: TextView,
            imageButton: ImageButton,
            progress: ProgressBar,
        ) {
            progress.visibility = View.GONE
            textView.text = message
            imageView.visibility = View.VISIBLE
            textView.visibility = View.VISIBLE
            imageButton.visibility = View.VISIBLE
        }
    }

    object Loading: TracksUiState(){

        override fun apply(
            imageView: ImageView,
            textView: TextView,
            imageButton: ImageButton,
            progress: ProgressBar,
        ) {
            progress.visibility = View.VISIBLE
            imageView.visibility = View.GONE
            textView.visibility = View.GONE
            imageButton.visibility = View.GONE
        }

    }




}
