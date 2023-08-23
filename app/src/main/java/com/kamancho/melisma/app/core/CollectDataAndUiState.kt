package com.kamancho.melisma.app.core

import com.kamancho.melisma.favorites.presentation.CollectData
import com.kamancho.melisma.trending.presentation.CollectUiState

/**
 * Created by HP on 23.05.2023.
 **/
interface CollectDataAndUiState<T,E>: CollectUiState<T>, CollectData<E>