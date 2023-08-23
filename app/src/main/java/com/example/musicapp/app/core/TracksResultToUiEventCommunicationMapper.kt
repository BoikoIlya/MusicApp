package com.example.musicapp.app.core

import androidx.media3.common.MediaItem
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.musicdialog.presentation.AddTrackDialogFragment
import com.example.musicapp.player.presentation.PlayingTrackIdCommunication
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface TracksResultToUiEventCommunicationMapper: TracksResult.Mapper<Unit>{

    class Base @Inject constructor(
        private val singleUiEventCommunication: GlobalSingleUiEventCommunication,
        private val playingTrackIdCommunication: PlayingTrackIdCommunication
    ) : TracksResultToUiEventCommunicationMapper{

        override suspend fun map(message: String, list: List<MediaItem>, error: Boolean,newId: Int) {
            if(error) {
                singleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(message))
                return
            }

            if(newId!=-1) playingTrackIdCommunication.map(newId)

            if(message.isNotEmpty())
                singleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Success(message))
            else
                singleUiEventCommunication.map(SingleUiEventState.ShowDialog(AddTrackDialogFragment()))
        }

    }
}