package com.example.musicapp.hlscachesystem.presentation

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.exoplayer.scheduler.PlatformScheduler
import androidx.media3.exoplayer.scheduler.Scheduler
import com.example.musicapp.R
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.hlscachesystem.data.HlsCacheQueueStore
import com.example.musicapp.hlscachesystem.data.HlsDownloaderCacheRepository
import com.example.musicapp.main.di.App
import com.example.musicapp.main.di.AppComponent
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 08.08.2023.
 **/
//HLS - http live streaming protocol m3u8 file format
class HLSCacheService: LifecycleService() {

    @Inject
    lateinit var cache: HlsCacheQueueStore

    @Inject
    lateinit var dispatchersList: DispatchersList

    @Inject
    lateinit var repository: HlsDownloaderCacheRepository

    @Inject
    lateinit var hlsCachingResultCommunication: HLSCachingResultCommunication

    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannelCacheName: String
    private lateinit var notificationChannel: NotificationChannel

    companion object{
        private const val notification_channel_cache_id = "notification_channel_cache_id"
        private const val notificationId = 2
        private const val group_id = "download_group"
        private const val group_name = "Download Group"
    }

    private lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        component = (this.applicationContext as App).appComponent
        component.inject(this)
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationChannelCacheName = getString(R.string.cache_track_channel_name)

        val notificationChannelGroup = NotificationChannelGroup(group_id, group_name)
        notificationManager.createNotificationChannelGroup(notificationChannelGroup)


        notificationChannel = NotificationChannel(
            notification_channel_cache_id,
            notificationChannelCacheName,
            NotificationManager.IMPORTANCE_LOW
        )

        notificationChannel.group = group_id
    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        notificationManager.createNotificationChannel(notificationChannel)

        val initialNotification =
            Notification.Builder(this@HLSCacheService, notification_channel_cache_id)
                .setColor(ContextCompat.getColor(this, R.color.orange))
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentTitle(getString(R.string.preparing_for_download))
                .build()

        notificationManager.notify(notificationId, initialNotification)

        startForeground(notificationId, initialNotification)

        lifecycleScope.launch(dispatchersList.io()) {


                while (true) {

                    val mediaItem = cache.mediaItem()

                    if (mediaItem == null) {
                        stopSelf()
                        return@launch
                    }
                    try {
                    var lastPercentage = 0

                    repository.download(mediaItem!!) { contentLength, bytesDownloaded, percentDownloaded ->
                        if (lastPercentage != percentDownloaded.toInt()) {
                            notificationManager.notify(
                                notificationId,
                                Notification.Builder(this@HLSCacheService, notification_channel_cache_id)
                                    .setColor(ContextCompat.getColor(this@HLSCacheService, R.color.orange))
                                    .setSmallIcon(android.R.drawable.stat_sys_download)
                                    .setContentText(mediaItem.mediaMetadata.title.toString() + getString(R.string.dash) + mediaItem.mediaMetadata.artist)
                                    .setContentTitle("${percentDownloaded.toInt()}%")
                                    .setProgress(100, percentDownloaded.toInt(), false)
                                    .build()
                            )
                        }

                        if (percentDownloaded.toInt() == 100) {
                            hlsCachingResultCommunication.map(HLSCachingResult.Completed)
                            notificationManager.notify(
                                mediaItem.mediaId.toInt(),
                                Notification.Builder(this@HLSCacheService, notification_channel_cache_id)
                                    .setColor(ContextCompat.getColor(this@HLSCacheService, R.color.orange))
                                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                                    .setContentText(mediaItem.mediaMetadata.title.toString() + getString(R.string.dash) + mediaItem.mediaMetadata.artist)
                                    .setContentTitle(getString(R.string.successfully_downloaded))
                                    .build()
                            )
                        }

                    }

                }catch (e:Exception){
                        Log.d("tag", "onStartCommand: $e")
                        e.printStackTrace()
                    notificationManager.notify(
                        mediaItem.mediaId.toInt(),
                        Notification.Builder(this@HLSCacheService, notification_channel_cache_id)
                            .setColor(ContextCompat.getColor(this@HLSCacheService, R.color.orange))
                            .setSmallIcon(android.R.drawable.stat_sys_download_done)
                            .setContentText(mediaItem.mediaMetadata.title.toString() + getString(R.string.dash) + mediaItem.mediaMetadata.artist)
                            .setContentTitle(getString(R.string.fail_to_download))
                            .build()
                    )
                }
                }

        }
        return START_STICKY
    }
}

