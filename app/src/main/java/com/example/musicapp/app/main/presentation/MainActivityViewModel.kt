package com.example.musicapp.app.main.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

/**
 * Created by HP on 31.01.2023.
 **/
class MainActivityViewModel @Inject constructor(
    private val playerCommunication: PlayerCommunication
):ViewModel() {

     suspend fun collect(
        lifecycleOwner: LifecycleOwner,
        collector: FlowCollector<BottomPlayerBarState>,
    ) = playerCommunication.collectBottomBarState(lifecycleOwner, collector)

    fun playerAction(state: PlayerCommunicationState){
        playerCommunication.map(state)
    }

}