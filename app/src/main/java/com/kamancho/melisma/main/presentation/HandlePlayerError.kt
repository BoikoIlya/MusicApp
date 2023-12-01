package com.kamancho.melisma.main.presentation

import android.util.Log
import androidx.media3.common.PlaybackException
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.ConnectionChecker
import com.kamancho.melisma.app.core.ManagerResource
import javax.inject.Inject

/**
 * Created by HP on 03.07.2023.
 **/
interface HandlePlayerError {

    suspend fun handle(e: PlaybackException): PlayerErrorState

    class Base @Inject constructor(
        private val managerResource: ManagerResource,
        private val connectionChecker: ConnectionChecker,
    ) : HandlePlayerError {

        override suspend fun handle(e: PlaybackException): PlayerErrorState {
            Log.d("tag", "handle: ${e.errorCodeName}")
            if (
                (e.errorCode == PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED
                        || e.errorCode == PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND)
                && connectionChecker.isDeviceHaveConnection()
            )
                return PlayerErrorState.SeekToNext(managerResource.getString(R.string.unavailable_track))

            val messageId =
                when (e.errorCode) {
                    PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> R.string.no_connection_message
                    PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> R.string.bad_connection_quality
                    PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND -> R.string.no_file_message
                    else -> null
                }
            val message =
                if (messageId != null) managerResource.getString(messageId) else e.errorCodeName
            return PlayerErrorState.ShowError(message)
        }

    }
}