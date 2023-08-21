package com.example.musicapp.editplaylist.presentation

import com.example.musicapp.app.core.Mapper
import com.example.musicapp.creteplaylist.presentation.PlaylistDataSaveBtnUiState
import com.example.musicapp.creteplaylist.presentation.PlaylistDataUiState
import com.example.musicapp.creteplaylist.presentation.PlaylistDataUiStateCommunication
import com.example.musicapp.creteplaylist.presentation.PlaylistSaveBtnUiStateCommunication
import javax.inject.Inject


/**
 * Created by HP on 18.07.2023.
 **/
interface EditPlaylistUpdateMapper: Mapper<String,Unit> {

    class Base @Inject constructor(
        private val playlistUiStateCommunication: PlaylistDataUiStateCommunication,
        private val btnUiStateCommunication: PlaylistSaveBtnUiStateCommunication
    ): EditPlaylistUpdateMapper {

        override fun map(data: String) {
            if(data.isNotEmpty()){
                playlistUiStateCommunication.map(PlaylistDataUiState.Error(data))
                btnUiStateCommunication.map(PlaylistDataSaveBtnUiState.Hide)
            } else playlistUiStateCommunication.map(PlaylistDataUiState.Success)
        }
    }
}