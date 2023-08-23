package com.kamancho.melisma.app.core

import androidx.media3.common.MediaItem

/**
 * Created by HP on 23.05.2023.
 **/
class TracksResultEmptyMapper: TracksResultToUiEventCommunicationMapper {
    override suspend fun map(message: String, list: List<MediaItem>, error: Boolean,newId: Int) = Unit

}