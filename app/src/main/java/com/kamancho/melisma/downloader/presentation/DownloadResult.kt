package com.kamancho.melisma.downloader.presentation

import com.kamancho.melisma.app.core.CacheFetcher
import com.kamancho.melisma.favorites.presentation.FavoritesTracksViewModel
import com.kamancho.melisma.favoritesplaylistdetails.presentation.PlaylistDetailsViewModel

/**
 * Created by HP on 09.08.2023.
 **/
sealed interface DownloadResult{

    fun apply(viewModel: FavoritesTracksViewModel)

    fun apply(viewModel: CacheFetcher, query: String)

    object Empty: DownloadResult {
        override fun apply(viewModel: FavoritesTracksViewModel) = Unit
        override fun apply(viewModel: CacheFetcher, query: String) = Unit
    }

    object Completed: DownloadResult {
        override fun apply(viewModel: FavoritesTracksViewModel) {
            viewModel.fetchData()
            viewModel.clearDownloadState()
        }

        override fun apply(viewModel: CacheFetcher, query: String) {
            viewModel.fetchFromCache(query)
            viewModel.clearDownloadCommunication()
        }
    }

}