package com.kamancho.melisma.userplaylists.presentation

import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.userplaylists.domain.PlaylistsResult
import javax.inject.Inject

/**
 * Created by HP on 13.07.2023.
 **/

interface PlaylistsResultUpdateToUiEventMapper: PlaylistsResult.Mapper<Unit> {


    class Base @Inject constructor(
        private val globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
    ) : PlaylistsResultUpdateToUiEventMapper {

        override suspend fun map(message: String, error: Boolean) {
            globalSingleUiEventCommunication.map(
                if(error)
                    SingleUiEventState.ShowSnackBar.Error(message)
                else
                    SingleUiEventState.ShowSnackBar.Success(message))

        }
    }
}