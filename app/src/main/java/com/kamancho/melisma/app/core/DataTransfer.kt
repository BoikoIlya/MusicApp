package com.kamancho.melisma.app.core

import com.kamancho.melisma.trending.domain.TrackDomain
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import javax.inject.Inject

/**
 * Created by HP on 21.03.2023.
 **/
interface DataTransfer<T> {

    fun read(): T?

    fun save(data: T)

    abstract class Abstract<T>: DataTransfer<T>
    {
        private var data: T? =null

        override fun read(): T? = data

        override fun save(data: T) {
            this.data = data
        }

    }

    class MusicDialogTransfer @Inject constructor():
        DataTransfer<TrackDomain>, Abstract<TrackDomain>()


    class PlaylistTransfer @Inject constructor():
            DataTransfer<PlaylistDomain>, Abstract<PlaylistDomain>()



}

