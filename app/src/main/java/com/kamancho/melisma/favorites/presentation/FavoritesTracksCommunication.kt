package com.kamancho.melisma.favorites.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.Communication
import kotlinx.coroutines.flow.FlowCollector

/**
 * Created by HP on 23.05.2023.
 **/

interface FavoritesUiCommunication<T>: UiCommunication<FavoritesUiState,T>,CollectLoadingCommunication{

    fun showLoading(state: FavoritesUiState)

    abstract class Abstract<T>(
        uiStateCommunication: Communication.Mutable<FavoritesUiState>,
        favoritesListCommunication: Communication.Mutable<List<T>>,
        private val favoritesTracksLoadingCommunication: Communication.Mutable<FavoritesUiState>
    ): FavoritesUiCommunication<T>, UiCommunication.Abstract<FavoritesUiState,T>(
        uiStateCommunication,favoritesListCommunication
    ){

        override fun showLoading(state: FavoritesUiState) = favoritesTracksLoadingCommunication.map(state)

        override suspend fun collectLoading(
            owner: LifecycleOwner,
            collector: FlowCollector<FavoritesUiState>
        ) = favoritesTracksLoadingCommunication.collect(owner,collector)
    }

}

interface CollectLoadingCommunication{

    suspend fun collectLoading(owner: LifecycleOwner,collector: FlowCollector<FavoritesUiState>)
}

interface FavoritesTracksCommunication:FavoritesUiCommunication<MediaItem> {

    class Base (
        uiStateCommunication: FavoritesStateCommunication,
        favoritesTracksCommunication: FavoritesTrackListCommunication,
        favoritesTracksLoadingCommunication: FavoritesTracksLoadingCommunication
    ) : FavoritesUiCommunication.Abstract<MediaItem>(
        uiStateCommunication,
        favoritesTracksCommunication,
        favoritesTracksLoadingCommunication
    ),FavoritesTracksCommunication

}