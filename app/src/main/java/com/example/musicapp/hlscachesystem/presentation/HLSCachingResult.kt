package com.example.musicapp.hlscachesystem.presentation

import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.FavoritesViewModel
import com.example.musicapp.editplaylist.presentation.EditPlaylistViewModel
import com.example.musicapp.favorites.presentation.FavoritesTracksViewModel
import com.example.musicapp.favorites.presentation.FavoritesUiState

/**
 * Created by HP on 09.08.2023.
 **/
sealed interface HLSCachingResult{

    fun apply(viewModel: FavoritesTracksViewModel)

    object Empty: HLSCachingResult{
        override fun apply(viewModel: FavoritesTracksViewModel) = Unit
    }

    object Completed: HLSCachingResult {
        override fun apply(viewModel: FavoritesTracksViewModel) {
           viewModel.fetchData()
            viewModel.clearHlsCachingState()
        }
    }

}