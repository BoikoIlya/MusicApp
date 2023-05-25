package com.example.musicapp.app.core

import androidx.media3.common.MediaItem
import com.example.musicapp.favorites.presentation.TracksResult

/**
 * Created by HP on 23.05.2023.
 **/
class TracksResultEmptyMapper: TracksResultToUiEventCommunicationMapper {
    override suspend fun map(message: String, list: List<MediaItem>) = Unit
    override suspend fun map(
        message: String,
        list: List<MediaItem>,
        albumDescription: String,
        albumName: String,
        albumImgUrl: String,
    ) = Unit
}