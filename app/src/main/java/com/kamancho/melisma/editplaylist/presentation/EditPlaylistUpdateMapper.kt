package com.kamancho.melisma.editplaylist.presentation

import com.kamancho.melisma.app.core.Mapper
import com.kamancho.melisma.creteplaylist.presentation.PlaylistDataSaveBtnUiState
import com.kamancho.melisma.creteplaylist.presentation.PlaylistDataUiState
import com.kamancho.melisma.creteplaylist.presentation.PlaylistDataUiStateCommunication
import com.kamancho.melisma.creteplaylist.presentation.PlaylistSaveBtnUiStateCommunication
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