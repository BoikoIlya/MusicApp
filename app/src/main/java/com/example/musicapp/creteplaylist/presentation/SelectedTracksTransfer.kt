package com.example.musicapp.creteplaylist.presentation

import android.view.View
import androidx.media3.common.MediaItem
import com.example.musicapp.R
import com.example.musicapp.addtoplaylist.domain.SelectedTrackDomain
import com.example.musicapp.addtoplaylist.presentation.SelectedTrackUi
import com.example.musicapp.app.core.CopyMediaItemMapper
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.ToMediaItemMapper
import java.util.LinkedList
import javax.inject.Inject

/**
 * Created by HP on 16.07.2023.
 **/

interface SelectedTracksStore {


    suspend fun saveList(list: List<SelectedTrackDomain>)

    suspend fun read():LinkedList<SelectedTrackDomain>

    class Base @Inject constructor() : SelectedTracksStore {


        private val data = LinkedList<SelectedTrackDomain>()



        override suspend fun saveList(list: List<SelectedTrackDomain>) {
            data.clear()
            data.addAll(list)
        }

        override suspend fun read(): LinkedList<SelectedTrackDomain> {
            val newList = LinkedList<SelectedTrackDomain>()
            newList.addAll(data)
            return newList
        }
    }
}