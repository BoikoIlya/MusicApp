package com.example.musicapp.player.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by HP on 02.02.2023.
 **/
class PlayerServiceBreaker: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.stopService(Intent(context, PlayerService::class.java))
    }
}