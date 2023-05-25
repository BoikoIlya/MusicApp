package com.example.musicapp.favorites.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.CollectTracksAndUiState
import com.example.musicapp.app.core.Communication
import kotlinx.coroutines.flow.FlowCollector

/**
 * Created by HP on 20.03.2023.
 **/
interface TracksCommunication<T>:  CollectTracksAndUiState<T> {

    fun showUiState(state: T)

    fun showTracks(tracks: List<MediaItem>)


    abstract class Abstract<T> (
        private val stateCommunication: Communication.Mutable<T>,
        private val tracksCommunication: Communication.Mutable<List<MediaItem>>
    ): TracksCommunication<T> {

        override fun showUiState(state: T) = stateCommunication.map(state)
        override fun showTracks(tracks: List<MediaItem>) = tracksCommunication.map(tracks)

        override suspend fun collectState(
            owner: LifecycleOwner,
            collector: FlowCollector<T>,
        ) = stateCommunication.collect(owner, collector)

        override suspend fun collectTracks(
            owner: LifecycleOwner,
            collector: FlowCollector<List<MediaItem>>,
        ) = tracksCommunication.collect(owner,collector)


    }

    class EmptyCommunication: TracksCommunication<Unit> {
        override fun showUiState(state: Unit) = Unit
        override fun showTracks(tracks: List<MediaItem>) = Unit
        override suspend fun collectTracks(owner: LifecycleOwner, collector: FlowCollector<List<MediaItem>>) = Unit
        override suspend fun collectState(owner: LifecycleOwner, collector: FlowCollector<Unit>)= Unit
    }

}




interface CollectTracks{

    suspend fun collectTracks(owner: LifecycleOwner, collector: FlowCollector<List<MediaItem>>)
}
