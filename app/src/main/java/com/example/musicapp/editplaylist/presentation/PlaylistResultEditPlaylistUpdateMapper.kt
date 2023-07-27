package com.example.musicapp.editplaylist.presentation

import com.example.musicapp.creteplaylist.presentation.PlaylistDataSaveBtnUiState
import com.example.musicapp.creteplaylist.presentation.PlaylistDataUiState
import com.example.musicapp.creteplaylist.presentation.PlaylistDataUiStateCommunication
import com.example.musicapp.creteplaylist.presentation.PlaylistSaveBtnUiStateCommunication
import com.example.musicapp.userplaylists.domain.PlaylistsResult
import javax.inject.Inject


/**
 * Created by HP on 18.07.2023.
 **/
interface PlaylistResultEditPlaylistUpdateMapper:  PlaylistsResult.Mapper<Unit> {

    class Base @Inject constructor(
        private val playlistUiStateCommunication: PlaylistDataUiStateCommunication,
        private val btnUiStateCommunication: PlaylistSaveBtnUiStateCommunication
    ): PlaylistResultEditPlaylistUpdateMapper {

        override suspend fun map(message: String, error: Boolean) {
           if(error){
               playlistUiStateCommunication.map(PlaylistDataUiState.Error(message))
               btnUiStateCommunication.map(PlaylistDataSaveBtnUiState.Hide)
           }
            else playlistUiStateCommunication.map(PlaylistDataUiState.Success)
           }
    }
}