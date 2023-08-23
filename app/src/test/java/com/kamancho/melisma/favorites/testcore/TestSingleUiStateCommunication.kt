package com.kamancho.melisma.favorites.testcore

import androidx.lifecycle.LifecycleOwner
import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.app.core.SingleUiEventState
import kotlinx.coroutines.flow.FlowCollector

/**
 * Created by HP on 29.04.2023.
 **/
class TestSingleUiStateCommunication: GlobalSingleUiEventCommunication {
    val stateList = emptyList<SingleUiEventState>().toMutableList()

    override suspend fun collect(
        lifecycleOwner: LifecycleOwner,
        collector: FlowCollector<SingleUiEventState>,
    ) = Unit
    override suspend fun map(newValue: SingleUiEventState) {
        stateList.add(newValue)
    }

}


