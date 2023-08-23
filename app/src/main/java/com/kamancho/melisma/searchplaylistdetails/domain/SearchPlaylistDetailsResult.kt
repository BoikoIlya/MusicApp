package com.kamancho.melisma.trending.presentation

import com.kamancho.melisma.trending.domain.TrackDomain
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain

/**
 * Created by HP on 27.01.2023.
 **/
sealed interface SearchPlaylistDetailsResult {

   suspend fun <T>map(mapper: Mapper<T>):T

    interface Mapper<T>{
        suspend fun map(playlistData: PlaylistDomain?, tracks: List<TrackDomain>, message: String): T
    }

    data class Success(
        private val playlistData: PlaylistDomain,
        private val tracks: List<TrackDomain>
    ): SearchPlaylistDetailsResult {
        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map(playlistData,tracks, "")
    }

    data class Error(
        private val message: String,
    ): SearchPlaylistDetailsResult {
        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map(null, emptyList(), message)
    }

}