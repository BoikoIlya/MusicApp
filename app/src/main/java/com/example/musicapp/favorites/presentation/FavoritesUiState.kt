package com.example.musicapp.favorites.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.simform.refresh.SSPullToRefreshLayout

/**
 * Created by HP on 21.03.2023.
 **/


sealed class FavoritesUiState {

    abstract fun apply(
        textView: TextView,
        progress: SSPullToRefreshLayout,
        recyclerView: RecyclerView,
    )

    object Success: FavoritesUiState(){

        override fun apply(
            textView: TextView,
            progress: SSPullToRefreshLayout,
            recyclerView: RecyclerView,
        ) {
            progress.setRefreshing(false)
            progress.isEnabled = false
            progress.isEnabled = true
            textView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }


    object Loading: FavoritesUiState(){

        override fun apply(
            textView: TextView,
            progress: SSPullToRefreshLayout,
            recyclerView: RecyclerView,
        ) {
            progress.setRefreshing(true)
        }

    }

    object Failure: FavoritesUiState(){

        override fun apply(
            textView: TextView,
            progress: SSPullToRefreshLayout,
            recyclerView: RecyclerView,
        ) {
            progress.setRefreshing(false)
            progress.isEnabled = false
            progress.isEnabled = true
            textView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

    }




}