package com.kamancho.melisma.creteplaylist.presentation

import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.userplaylists.domain.PlaylistsResult
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