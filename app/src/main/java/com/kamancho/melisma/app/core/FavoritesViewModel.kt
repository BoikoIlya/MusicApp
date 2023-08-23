package com.kamancho.melisma.app.core

import com.kamancho.melisma.favorites.presentation.CollectLoadingCommunication

/**
 * Created by HP on 14.07.2023.
 **/
interface FavoritesViewModel<E,T>: CollectDataAndUiState<E,T>, CollectLoadingCommunication{

    fun update(loading: Boolean)
}