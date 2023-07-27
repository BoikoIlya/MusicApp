package com.example.musicapp.creteplaylist.presentation

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
        private val playlistDataUiStateCommunication: PlaylistDataUiStateCommunication
    ): PlaylistDataResultMapper {

        override suspend fun map(message: String, error: Boolean) {
            if(error) {
                globalSingleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(message))
                playlistDataUiStateCommunication.map(PlaylistDataUiState.DisableLoading)
            }else {
                globalSingleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Success(message))
                playlistDataUiStateCommunication.map(PlaylistDataUiState.PopFragment)
            }
        }
    }
}