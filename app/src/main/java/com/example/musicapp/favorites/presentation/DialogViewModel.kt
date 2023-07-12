package com.example.musicapp.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.DataTransfer


/**
 * Created by HP on 26.06.2023.
 **/
abstract class DialogViewModel<T>(
    private val cache: DataTransfer<T>,
): ViewModel() {

    fun fetchData() = cache.read()
}