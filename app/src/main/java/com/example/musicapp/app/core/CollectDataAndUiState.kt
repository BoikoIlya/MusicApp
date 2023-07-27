package com.example.musicapp.app.core

import com.example.musicapp.favorites.presentation.CollectData
import com.example.musicapp.trending.presentation.CollectUiState

/**
 * Created by HP on 23.05.2023.
 **/
interface CollectDataAndUiState<T,E>: CollectUiState<T>, CollectData<E>