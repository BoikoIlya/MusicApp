package com.kamancho.melisma.main.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.PlayerControlsCommunication
import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.trending.presentation.MediaControllerWrapper
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

/**
 * Created by HP on 31.01.2023.
 **/
interface PlayerCommunication: CollectPlayerControls, CollectCurrentQueue, CollectSelectedTrack {



    fun map(state: PlayerCommunicationState)


    class Base @Inject constructor(
        private val playerControlsCommunication: PlayerControlsCommunication,
        private val currentQueueCommunication: CurrentQueueCommunication,
        private val selectedTrackCommunication: SelectedTrackCommunication,
        private val singleUiEventCommunication: GlobalSingleUiEventCommunication,
        private val trackDurationCommunication: TrackDurationCommunication,
        private val controller: MediaControllerWrapper,
    ): PlayerCommunication{


        override fun map(state: PlayerCommunicationState) {

           state.apply(
                playerControlsCommunication,
                currentQueueCommunication,
                selectedTrackCommunication,
                controller,
               singleUiEventCommunication,
               trackDurationCommunication
            )
        }


        override suspend fun collectPlayerControls(
            owner: LifecycleOwner,
            collector: FlowCollector<PlayerControlsState>,
        ) = playerControlsCommunication.collect(owner,collector)

        override suspend fun collectCurrentQueue(
            owner: LifecycleOwner,
            collector: FlowCollector<List<MediaItem>>,
        ) = currentQueueCommunication.collect(owner,collector)

        override suspend fun collectSelectedTrack(
            owner: LifecycleOwner,
            collector: FlowCollector<MediaItem>,
        ) = selectedTrackCommunication.collect(owner,collector)

    }



}

interface CollectPlayerControls{

    suspend fun collectPlayerControls(
        owner: LifecycleOwner,
        collector: FlowCollector<PlayerControlsState>
    )
}

interface CollectCurrentQueue{

    suspend fun collectCurrentQueue(
        owner: LifecycleOwner,
        collector: FlowCollector<List<MediaItem>>
    )
}

interface CollectSelectedTrack{
    suspend fun collectSelectedTrack(
        owner: LifecycleOwner,
        collector: FlowCollector<MediaItem>,
    )

}


