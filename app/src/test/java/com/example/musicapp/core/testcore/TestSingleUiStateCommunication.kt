package com.example.musicapp.core.testcore

import androidx.lifecycle.LifecycleOwner
import com.example.musicapp.app.core.SingleUiEventCommunication
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.app.core.UiEventState
import com.example.musicapp.main.presentation.UiEventsCommunication
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


class TestUiEventsCommunication: UiEventsCommunication {
    val stateList = emptyList<UiEventState>().toMutableList()

    override suspend fun collect(
        lifecycleOwner: LifecycleOwner,
        collector: FlowCollector<UiEventState>,
    ) = Unit
    override  fun map(newValue: UiEventState) {
        stateList.add(newValue)
    }

}

