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
            progress.visibility = View.GONE
            imageView.visibility = View.GONE
            textView.visibility = View.GONE
            imageButton.visibility = View.GONE
        }
    }

    object PlayingTrack: TrendingUiState(){
        override fun apply(
            imageView: ImageView,
            textView: TextView,
            imageButton: ImageButton,
            progress: ProgressBar,
        ) {
                    //todo maybe should delete
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
            progress.visibility = View.GONE
            textView.text = message
            imageView.visibility = View.VISIBLE
            textView.visibility = View.VISIBLE
            imageButton.visibility = View.VISIBLE
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
            imageView.visibility = View.GONE
            textView.visibility = View.GONE
            imageButton.visibility = View.GONE
        }

    }




}
