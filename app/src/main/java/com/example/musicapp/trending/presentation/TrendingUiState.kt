package com.example.musicapp.trending.presentation

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

sealed class TrendingUiState {

    abstract fun apply(
        imageView: ImageView,
        textView: TextView,
        imageButton: ImageButton,
        progress: ProgressBar
    )

    object Success: TrendingUiState(){

        override fun apply(
            imageView: ImageView,
            textView: TextView,
            imageButton: ImageButton,
            progress: ProgressBar
        ) {
            imageView.visibility = View.GONE
            textView.visibility = View.GONE
            imageButton.visibility = View.GONE
            progress.visibility = View.GONE
        }
    }

    data class Error(
        private val message: String
    ): TrendingUiState(){

        override fun apply(
            imageView: ImageView,
            textView: TextView,
            imageButton: ImageButton,
            progress: ProgressBar
        ) {
            textView.text = message
            imageView.visibility = View.GONE
            textView.visibility = View.GONE
            imageButton.visibility = View.GONE
            progress.visibility = View.GONE
        }
    }

    object Loading: TrendingUiState(){

        override fun apply(
            imageView: ImageView,
            textView: TextView,
            imageButton: ImageButton,
            progress: ProgressBar
        ) {
            progress.visibility = View.VISIBLE
        }

    }

}
