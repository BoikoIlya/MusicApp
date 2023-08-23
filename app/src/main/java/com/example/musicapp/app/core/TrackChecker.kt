package com.example.musicapp.app.core

import androidx.media3.common.MediaItem
import com.example.musicapp.R
import com.example.musicapp.app.core.ToMediaItemMapper.Base.Companion.is_cached
import javax.inject.Inject

/**
 * Created by HP on 24.06.2023.
 **/
interface TrackChecker {

    suspend fun checkIfPlayable(item: MediaItem, playable: suspend ()->Unit)

    class Base @Inject constructor(
        private val singleUiEventCommunication: GlobalSingleUiEventCommunication,
        private val managerResource: ManagerResource,
        private val connectionChecker: ConnectionChecker
    ): TrackChecker {

        override suspend fun checkIfPlayable(item: MediaItem, playable: suspend()->Unit) {
            if(item.mediaMetadata.isPlayable!!){
                if(connectionChecker.isDeviceHaveConnection() || item.mediaMetadata.extras!!.getBoolean(is_cached))
                    playable.invoke()
                else singleUiEventCommunication.map(
                    SingleUiEventState.ShowSnackBar.Error(
                        managerResource.getString(R.string.no_connection_message)
                    )
                )
            }
            else {
                singleUiEventCommunication.map(
                    SingleUiEventState.ShowSnackBar.Error(
                        managerResource.getString(R.string.unavailable_track)
                    )
                )
            }

        }
    }

    object Empty: TrackChecker {
        override suspend fun checkIfPlayable(item: MediaItem, playable: suspend () -> Unit) = Unit
    }
}