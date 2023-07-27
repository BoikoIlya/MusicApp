package com.example.musicapp.addtoplaylist.presentation

import android.util.Log
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 16.07.2023.
 **/
interface CachedTracksCommunication: Communication.Mutable<List<SelectedTrackUi>> {

    class Base @Inject constructor(): CachedTracksCommunication,Communication.UiUpdate<List<SelectedTrackUi>>(
        emptyList())
}