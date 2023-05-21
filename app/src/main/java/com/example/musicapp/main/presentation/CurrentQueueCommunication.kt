package com.example.musicapp.main.presentation

import android.util.Log
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.Communication
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by HP on 31.01.2023.
 **/
interface CurrentQueueCommunication: Communication.Mutable<List<MediaItem>> {


    class Base @Inject constructor(): Communication.UiUpdate<List<MediaItem>>(emptyList()),
        CurrentQueueCommunication

}