package com.example.musicapp.trending.main.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import com.example.musicapp.main.presentation.BottomPlayerBarState
import com.example.musicapp.app.main.presentation.PlayerCommunication
import com.example.musicapp.app.main.presentation.PlayerCommunicationState
import com.example.musicapp.player.presentation.PlayerServiceState
import kotlinx.coroutines.flow.FlowCollector

/**
 * Created by HP on 17.03.2023.
 **/
class TestPlayerCommunication:PlayerCommunication {
    val stateList = emptyList<PlayerCommunicationState>().toMutableList()

    override fun map(state: PlayerCommunicationState) {
        stateList.add(state)
    }

    override suspend fun collectBottomBarState(
        owner: LifecycleOwner,
        collector: FlowCollector<BottomPlayerBarState>,
    ) = Unit

    override suspend fun collectPlayerServiceState(
        owner: LifecycleOwner,
        collector: FlowCollector<PlayerServiceState>,
    )  = Unit

    override suspend fun collectSelectedTrack(
        owner: LifecycleOwner,
        collector: FlowCollector<MediaItem>,
    )  = Unit
}