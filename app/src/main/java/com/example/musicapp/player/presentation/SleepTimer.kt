package com.example.musicapp.player.presentation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.main.presentation.PlayerCommunicationState
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
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