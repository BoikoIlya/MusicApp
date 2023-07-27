package com.example.musicapp.creteplaylist.presentation

import android.util.Log
import androidx.media3.common.MediaItem
import com.example.musicapp.addtoplaylist.presentation.SelectedTrackUi
import com.example.musicapp.app.core.Communication
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.track_id
import javax.inject.Inject

/**
 * Created by HP on 16.07.2023.
 **/
interface SelectedTracksCommunication: Communication.Mutable<List<SelectedTrackUi>> {

    object Base  : SelectedTracksCommunication,Communication.UiUpdate<List<SelectedTrackUi>>(emptyList())


}