package com.example.musicapp.favorites.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.CollectDataAndUiState
import com.example.musicapp.app.core.Communication
import kotlinx.coroutines.flow.FlowCollector

/**
 * Created by HP on 20.03.2023.
 **/
interface UiCommunication<T,E>:  CollectDataAndUiState<T,E> {

    fun showUiState(state: T)

    fun showData(tracks: List<E>)


    abstract class Abstract<T,E> (
        private val stateCommunication: Communication.Mutable<T>,
        private val tracksCommunication: Communication.Mutable<List<E>>
    ): UiCommunication<T,E> {

        override fun showUiState(state: T) = stateCommunication.map(state)
        override fun showData(tracks: List<E>) = tracksCommunication.map(tracks)

        override suspend fun collectState(
            owner: LifecycleOwner,
            collector: FlowCollector<T>,
        ) = stateCommunication.collect(owner, collector)

        override suspend fun collectData(
            owner: LifecycleOwner,
            collector: FlowCollector<List<E>>,
        ) = tracksCommunication.collect(owner,collector)


    }

    class EmptyCommunication: UiCommunication<Unit,MediaItem> {
        override fun showUiState(state: Unit) = Unit
        override fun showData(tracks: List<MediaItem>) = Unit
        override suspend fun collectData(owner: LifecycleOwner, collector: FlowCollector<List<MediaItem>>) = Unit
        override suspend fun collectState(owner: LifecycleOwner, collector: FlowCollector<Unit>)= Unit
    }

}




interface CollectData<E>{

    suspend fun collectData(owner: LifecycleOwner, collector: FlowCollector<List<E>>)
}
