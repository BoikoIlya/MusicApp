package com.kamancho.melisma.trending.presentation

import android.os.Bundle
import androidx.navigation.NavController
import com.kamancho.melisma.R
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi

/**
 * Created by HP on 07.08.2023.
 **/
sealed interface TrendingTopBarNavigationState{

    fun apply(navController: NavController)

    data class Navigate(
        private val destination: Int
    ): TrendingTopBarNavigationState {

        override fun apply(navController: NavController) {
            navController.navigate(destination)
        }
    }

    data class NavigateToVkEmbededPlaylist(
        private val item: PlaylistUi
    ): TrendingTopBarNavigationState {

        companion object{
            private const val playlist_item_key ="playlistItem"
        }

        override fun apply(navController: NavController) {
            val bundle = Bundle();
            bundle.putParcelable(playlist_item_key,item)
            navController.navigate(R.id.action_trendingFragment_to_searchPlaylistDetailsFragment2,bundle)
        }
    }

    object Empty: TrendingTopBarNavigationState {
        override fun apply(navController: NavController) = Unit
    }
}