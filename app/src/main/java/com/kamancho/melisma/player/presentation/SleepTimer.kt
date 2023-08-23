package com.kamancho.melisma.player.presentation

import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.main.presentation.PlayerCommunication
import com.kamancho.melisma.main.presentation.PlayerCommunicationState
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by HP on 23.08.2023.
 **/
interface SleepTimer {

    suspend fun setupTimer(durationMin: Long)

    class Base @Inject constructor(
        private val communication: PlayerCommunication,
        private val dispatchersList: DispatchersList
    ): SleepTimer {



        override suspend  fun setupTimer(durationMin: Long) {
            val triggerAtMillis = TimeUnit.MINUTES.toMillis(durationMin)/4 + System.currentTimeMillis()
            while (true){
                delay(1000)
                if(triggerAtMillis<System.currentTimeMillis()) {
                    withContext(dispatchersList.ui()) {
                        communication.map(PlayerCommunicationState.Pause)
                    }
                    break
                }
            }

        }

    }
}