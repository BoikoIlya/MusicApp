package com.kamancho.melisma.searchplaylistdetails.presentation

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.favorites.presentation.FavoritesUiState
import com.kamancho.melisma.favoritesplaylistdetails.presentation.PlaylistDetailsCommunication
import com.kamancho.melisma.trending.domain.TrackDomain
import com.kamancho.melisma.trending.presentation.SearchPlaylistDetailsResult
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 15.08.2023.
 **/
class BaseSearchPlaylistDetailsResult @Inject constructor(
    private val communication: PlaylistDetailsCommunication,
    private val toPlaylistUiMapper: PlaylistDomain.Mapper<PlaylistUi>,
    private val toMediaItemMapper: TrackDomain.Mapper<MediaItem>,
    private val globalSingleUiEventCommunication: GlobalSingleUiEventCommunication
): SearchPlaylistDetailsResult.Mapper<Unit> {
    override suspend fun map(
        playlistData: PlaylistDomain?,
        tracks: List<TrackDomain>,
        message: String,
    ) {
        if(message.isEmpty()){
            communication.showPlaylistData(playlistData!!.map(toPlaylistUiMapper))
            communication.showData(tracks.map { it.map(toMediaItemMapper) })
            communication.showUiState(FavoritesUiState.Success)
        }else {
            communication.showUiState(FavoritesUiState.Failure)
            globalSingleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(message))
        }
    }
}