package com.example.musicapp.downloader.presentation

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.musicapp.main.di.App
import javax.inject.Inject

/**
 * Created by HP on 23.08.2023.
 **/
class DownloadTrackBroadcastReceiver: BroadcastReceiver() {

    @Inject
    lateinit var communication: DownloadCompleteCommunication

    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as App).appComponent.inject(this)
        val action = intent.action
        if(DownloadManager.ACTION_DOWNLOAD_COMPLETE == action){
            communication.map(DownloadResult.Completed)
        }
    }
}
