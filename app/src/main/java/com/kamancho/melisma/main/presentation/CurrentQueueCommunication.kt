package com.kamancho.melisma.main.presentation

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 31.01.2023.
 **/
interface CurrentQueueCommunication: Communication.Mutable<List<MediaItem>> {


    class Base @Inject constructor(): Communication.UiUpdate<List<MediaItem>>(emptyList()),
        CurrentQueueCommunication

}