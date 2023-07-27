package com.example.musicapp.app.core

import androidx.media3.common.MediaItem
import kotlinx.coroutines.Job

/**
 * Created by HP on 23.05.2023.
 **/

interface DeleteItemDialog{

    fun launchDeleteItemDialog(item: MediaItem): Job

}

