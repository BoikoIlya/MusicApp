package com.example.musicapp.app.core

import androidx.media3.common.MediaItem
import javax.inject.Inject
import javax.inject.Singleton

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
        DataTransfer<MediaItem>, DataTransfer.Abstract<MediaItem>()


    interface UpdateDialogTransfer: DataTransfer<Pair<String,String>> {

        class Base @Inject constructor() :
            UpdateDialogTransfer, DataTransfer.Abstract<Pair<String,String>>()
    }


}

