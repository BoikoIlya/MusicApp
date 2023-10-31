package com.kamancho.melisma.app.core

import com.kamancho.melisma.favorites.presentation.CollectLoadingCommunication
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi

/**
 * Created by HP on 14.07.2023.
 **/
interface FavoritesViewModel<E,T>: CollectDataAndUiState<E,T>, CollectLoadingCommunication{

    //todo
    fun update(loading: Boolean){}

    fun update(id: String,loading: Boolean,shouldUpdate: Boolean) = Unit

    fun update(playlist: PlaylistUi,loading: Boolean,shouldUpdate: Boolean) = Unit
}