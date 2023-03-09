package com.example.musicapp.app.core

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import com.example.musicapp.player.presentation.PlayerService


/**
 * Created by HP on 02.02.2023.
 **/

interface PlayerServiceManager {

    fun start()
    fun stop()
    fun isServiceRunning(service: Class<*>): Boolean

    class Base(
        private val context: Context,
    ): PlayerServiceManager{

        override fun start() {
            context.startForegroundService(Intent(context,PlayerService::class.java))
        }

        override fun stop() {
            context.stopService(Intent(context,PlayerService::class.java))
        }

        override fun isServiceRunning(service: Class<*>): Boolean {
           val manager:ActivityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
//            val manager = ApplicationProvider.getApplicationContext<Context>().getSystemService(
//                ACTIVITY_SERVICE
//            ) as ActivityManager
                for (info in manager.getRunningServices(Int.MAX_VALUE)) {
                    if (service.name == info.service.className) return true
                }
            return false
        }

    }
}