package com.kamancho.melisma.creteplaylist.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 17.07.2023.
 **/
interface PlaylistSaveBtnUiStateCommunication: Communication.Mutable<PlaylistDataSaveBtnUiState> {

    class Base @Inject constructor(): PlaylistSaveBtnUiStateCommunication, Communication.UiUpdate<PlaylistDataSaveBtnUiState>(
        PlaylistDataSaveBtnUiState.Empty
    )
}