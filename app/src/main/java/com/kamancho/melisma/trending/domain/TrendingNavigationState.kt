package com.kamancho.melisma.trending.domain

import com.kamancho.melisma.trending.presentation.TrendingTopBarNavigationState
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi

/**
 * Created by Ilya Boiko @camancho
on 09.11.2023.
 **/
sealed interface TrendingNavigationState {

    fun map(mapper: PlaylistDomain.Mapper<PlaylistUi>): TrendingTopBarNavigationState

    data class SimpleNavigation(
     private val destination: Int,
    ) : TrendingNavigationState {

        override fun map(mapper: PlaylistDomain.Mapper<PlaylistUi>): TrendingTopBarNavigationState = TrendingTopBarNavigationState.Navigate(destination)

    }

 data class EmbeddedVkPlaylistNavigation(
  private val playlistDomain: PlaylistDomain,
 ): TrendingNavigationState {

  override fun map(mapper: PlaylistDomain.Mapper<PlaylistUi>): TrendingTopBarNavigationState =
   TrendingTopBarNavigationState.NavigateToVkEmbededPlaylist(playlistDomain.map(mapper))

 }

    object Empty: TrendingNavigationState {
        override fun map(mapper: PlaylistDomain.Mapper<PlaylistUi>): TrendingTopBarNavigationState = TrendingTopBarNavigationState.Empty
    }
}