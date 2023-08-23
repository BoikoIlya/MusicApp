package com.kamancho.melisma.creteplaylist.presentation

import com.kamancho.melisma.addtoplaylist.domain.SelectedTrackDomain
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