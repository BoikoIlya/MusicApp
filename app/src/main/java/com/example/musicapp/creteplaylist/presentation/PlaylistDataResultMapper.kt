package com.example.musicapp.creteplaylist.presentation

import android.util.Log
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.userplaylists.domain.PlaylistsResult
import javax.inject.Inject

/**
 * Created by HP on 18.07.2023.
 **/
interface PlaylistDataResultMapper: PlaylistsResult.Mapper<Unit> {

    class Base @Inject constructor(
        private val globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
        private val playlistDataUiStateCommunication: PlaylistDataUiStateCommunication,
        private val saveBtnUiStateCommunication: PlaylistSaveBtnUiStateCommunication
    ): PlaylistDataResultMapper {


        override suspend fun map(message: String, error: Boolean) {
            if(error) {
                globalSingleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(message))
                playlistDataUiStateCommunication.map(PlaylistDataUiState.DisableLoading)
                saveBtnUiStateCommunication.map(PlaylistDataSaveBtnUiState.Show)
            }else {
                globalSingleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Success(message))
                playlistDataUiStateCommunication.map(PlaylistDataUiState.PopFragment)
            }
        }
    }
}