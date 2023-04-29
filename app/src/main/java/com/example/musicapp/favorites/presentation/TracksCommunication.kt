package com.example.musicapp.favorites.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.Communication
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

/**
 * Created by HP on 20.03.2023.
 **/
interface TracksCommunication: CollectTracks {

    fun showUiState(state: FavoriteTracksUiState)

    fun showTracks(tracks: List<MediaItem>)


    class Base @Inject constructor(
        private val favoritesStateCommunication: FavoritesTracksStateCommunication,
        private val tracksCommunication: FavoritesTracksCommunication
    ): TracksCommunication {

        override fun showUiState(state: FavoriteTracksUiState) = favoritesStateCommunication.map(state)


        override fun showTracks(tracks: List<MediaItem>) = tracksCommunication.map(tracks)

        override suspend fun collectState(
            owner: LifecycleOwner,
            collector: FlowCollector<FavoriteTracksUiState>,
        ) = favoritesStateCommunication.collect(owner, collector)

        override suspend fun collectTracks(
            owner: LifecycleOwner,
            collector: FlowCollector<List<MediaItem>>,
        ) = tracksCommunication.collect(owner,collector)


    }

}

interface FavoritesTracksStateCommunication: Communication.Mutable<FavoriteTracksUiState>{
    class Base @Inject constructor():
        Communication.UiUpdate<FavoriteTracksUiState>(FavoriteTracksUiState.Loading),
        FavoritesTracksStateCommunication
}


interface FavoritesTracksCommunication: Communication.Mutable<List<MediaItem>>{

    class Base @Inject constructor():
        Communication.UiUpdate<List<MediaItem>>(emptyList()),
        FavoritesTracksCommunication
}

interface CollectTracks{

    suspend fun collectState(owner: LifecycleOwner, collector: FlowCollector<FavoriteTracksUiState>)

    suspend fun collectTracks(owner: LifecycleOwner, collector: FlowCollector<List<MediaItem>>)
}