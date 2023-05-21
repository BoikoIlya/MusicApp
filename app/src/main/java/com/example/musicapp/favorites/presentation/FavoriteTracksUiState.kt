package com.example.musicapp.favorites.presentation

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.media3.common.MediaItem
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.trending.presentation.TracksUiState

/**
 * Created by HP on 21.03.2023.
 **/


sealed class FavoriteTracksUiState {

    abstract fun apply(
        textView: TextView,
        progress: ProgressBar,
        recyclerView: RecyclerView
    )

    object Success: FavoriteTracksUiState(){

        override fun apply(
            textView: TextView,
            progress: ProgressBar,
            recyclerView: RecyclerView
        ) {
            progress.visibility = View.GONE
            textView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    object Failure: FavoriteTracksUiState(){

        override fun apply(
            textView: TextView,
            progress: ProgressBar,
            recyclerView: RecyclerView
        ) {
            progress.visibility = View.GONE
            recyclerView.visibility = View.GONE
            textView.visibility = View.VISIBLE
        }
    }

    object Loading: FavoriteTracksUiState(){

        override fun apply(
            textView: TextView,
            progress: ProgressBar,
            recyclerView: RecyclerView
        ) {
            progress.visibility = View.VISIBLE
            textView.visibility = View.GONE
        }

    }


}