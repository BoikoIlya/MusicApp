package com.kamancho.melisma.main.presentation

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by HP on 31.01.2023.
 **/
interface SelectedTrackCommunication: Communication.Mutable<MediaItem> {


    @Singleton
    class Base @Inject constructor(): Communication.UiUpdate<MediaItem>(MediaItem.EMPTY),
        SelectedTrackCommunication

}