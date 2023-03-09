package com.example.musicapp.player.presentation

import androidx.lifecycle.LifecycleOwner
import com.example.musicapp.app.main.presentation.PlayerCommunication
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

/**
 * Created by HP on 31.01.2023.
 **/
class PlayerViewModel @Inject constructor(
    private val playerCommunication: PlayerCommunication
) {

   suspend fun collect(
       owner: LifecycleOwner,
       flowCollector: FlowCollector<PlayerServiceState>
   ) = playerCommunication.collectPlayerServiceState(owner,flowCollector)

}