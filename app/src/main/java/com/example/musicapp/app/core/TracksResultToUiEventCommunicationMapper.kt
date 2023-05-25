package com.example.musicapp.app.core

import androidx.media3.common.MediaItem
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.musicdialog.presentation.MusicDialogFragment
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface TracksResultToUiEventCommunicationMapper: TracksResult.Mapper<Unit>{

    class Base @Inject constructor(
        private val singleUiEventCommunication: SingleUiEventCommunication,
    ) : TracksResultToUiEventCommunicationMapper{

        override suspend fun map(message: String, list: List<MediaItem>) {
            if(message.isNotEmpty())
                singleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Success(message))
            else
                singleUiEventCommunication.map(SingleUiEventState.ShowDialog(MusicDialogFragment()))
        }

        override suspend fun map(
            message: String,
            list: List<MediaItem>,
            albumDescription: String,
            albumName: String,
            albumImgUrl: String,
        ) = Unit
    }
}