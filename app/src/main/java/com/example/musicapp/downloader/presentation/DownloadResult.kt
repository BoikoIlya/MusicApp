package com.example.musicapp.downloader.presentation

import com.example.musicapp.favorites.presentation.FavoritesTracksViewModel

/**
 * Created by HP on 09.08.2023.
 **/
sealed interface DownloadResult{

    fun apply(viewModel: FavoritesTracksViewModel)

    object Empty: DownloadResult {
        override fun apply(viewModel: FavoritesTracksViewModel) = Unit
    }

    object Completed: DownloadResult {
        override fun apply(viewModel: FavoritesTracksViewModel) {
            viewModel.fetchData()
            viewModel.clearDownloadState()
        }
    }

}