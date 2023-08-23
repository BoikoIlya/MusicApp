package com.kamancho.melisma.favorites.presentation

import androidx.lifecycle.ViewModel
import com.kamancho.melisma.app.core.DataTransfer


/**
 * Created by HP on 26.06.2023.
 **/
abstract class DialogViewModel<T>(
    private val cache: DataTransfer<T>,
): ViewModel() {

    fun fetchData() = cache.read()
}