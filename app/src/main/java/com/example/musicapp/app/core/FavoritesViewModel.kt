package com.example.musicapp.app.core

import com.example.musicapp.favorites.presentation.CollectLoadingCommunication
import com.example.musicapp.favorites.presentation.FavoritesUiState
import com.example.musicapp.main.presentation.CollectPlayerControls

/**
 * Created by HP on 14.07.2023.
 **/
interface FavoritesViewModel<E,T>: CollectDataAndUiState<E,T>, CollectLoadingCommunication{

    fun update(loading: Boolean)
}