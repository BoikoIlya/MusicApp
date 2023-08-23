package com.kamancho.melisma.player.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kamancho.melisma.main.di.App
import com.kamancho.melisma.main.presentation.PlayerCommunication
import com.kamancho.melisma.main.presentation.PlayerCommunicationState
import javax.inject.Inject

/**
 * Created by HP on 26.06.2023.
 **/
class HeadPhonesReceiver: BroadcastReceiver() {

    @Inject
    lateinit var playerCommunication: PlayerCommunication

    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as App).appComponent
            .playerComponent()
            .build()
            .inject(this)

            playerCommunication.map(PlayerCommunicationState.Pause)
    }

}