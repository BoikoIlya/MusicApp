package com.example.musicapp.favorites.testcore

import androidx.lifecycle.LifecycleOwner
import com.example.musicapp.app.core.SingleUiEventCommunication
import com.example.musicapp.app.core.SingleUiEventState
import kotlinx.coroutines.flow.FlowCollector

/**
 * Created by HP on 29.04.2023.
 **/
class TestSingleUiStateCommunication: SingleUiEventCommunication {
    val stateList = emptyList<SingleUiEventState>().toMutableList()

    override suspend fun collect(
        lifecycleOwner: LifecycleOwner,
        collector: FlowCollector<SingleUiEventState>,
    ) = Unit
    override suspend fun map(newValue: SingleUiEventState) {
        stateList.add(newValue)
    }

}


