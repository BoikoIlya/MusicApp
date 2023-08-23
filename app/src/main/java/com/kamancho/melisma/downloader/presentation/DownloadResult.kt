package com.kamancho.melisma.downloader.presentation

import com.kamancho.melisma.favorites.presentation.FavoritesTracksViewModel

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