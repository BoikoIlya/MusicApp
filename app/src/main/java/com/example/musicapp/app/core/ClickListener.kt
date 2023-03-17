package com.example.musicapp.app.core

import androidx.media3.common.MediaItem

/**
 * Created by HP on 30.01.2023.
 **/
interface ClickListener<T> {

    fun onClick(data: T)
}

interface Selector<T>{
    fun onSelect(data: T, position: Int)
}