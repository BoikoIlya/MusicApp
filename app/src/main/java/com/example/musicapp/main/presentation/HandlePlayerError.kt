package com.example.musicapp.main.presentation

import android.util.Log
import androidx.media3.common.PlaybackException
import com.example.musicapp.R
import com.example.musicapp.app.core.ConnectionChecker
import com.example.musicapp.app.core.ManagerResource
import javax.inject.Inject

/**
 * Created by HP on 03.07.2023.
 **/
interface HandlePlayerError {

    suspend fun handle(e: PlaybackException, ): PlayerErrorState

    class Base @Inject constructor(
        private val managerResource: ManagerResource,
        private val connectionChecker: ConnectionChecker
    ): HandlePlayerError{

        override suspend fun handle(e: PlaybackException, ): PlayerErrorState {

            if(e.errorCode == PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED && connectionChecker.isDeviceHaveConnection())
                return PlayerErrorState.SeekToNext(managerResource.getString(R.string.unavailable_track))

           val message = managerResource.getString(
               when(e.errorCode){
                 PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED ->R.string.no_connection_message
                   else -> {
                       Log.d("tag", "handle player error: ${e.errorCode} ${e.errorCodeName}")
                        0
                   }
            }
           )

           return if (message.isEmpty()) PlayerErrorState.SeekToNext(message)
                    else PlayerErrorState.ShowError(message)
        }

    }
}