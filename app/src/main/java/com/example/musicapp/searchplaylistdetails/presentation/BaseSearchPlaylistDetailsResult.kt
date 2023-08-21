package com.example.musicapp.searchplaylistdetails.presentation

import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.favorites.presentation.FavoritesUiState
import com.example.musicapp.favoritesplaylistdetails.presentation.PlaylistDetailsCommunication
import com.example.musicapp.trending.domain.TrackDomain
import com.example.musicapp.trending.presentation.SearchPlaylistDetailsResult
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import com.example.musicapp.userplaylists.presentation.PlaylistUi
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