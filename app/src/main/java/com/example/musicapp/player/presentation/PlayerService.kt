package com.example.musicapp.player.presentation

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.media.session.PlaybackState
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.musicapp.app.main.di.App
import com.example.musicapp.app.main.presentation.MainActivity
import com.example.musicapp.app.main.presentation.PlayerCommunication
import com.example.musicapp.app.main.presentation.PlayerCommunicationState
import com.example.musicapp.player.di.PlayerServiceComponent
import javax.inject.Inject

@UnstableApi /**
 * Created by HP on 30.01.2023.
 **/
//class PlayerService: LifecycleService() {
//
//    companion object{ var isStarted = false }
//
//    @Inject
//    lateinit var notificationChannel: NotificationChannel
//
//    @Inject
//    lateinit var factory: ViewModelProvider.Factory
//
//    @Inject
//    lateinit var notificationBuilder: NotificationCompat.Builder
//
//    @Inject
//    lateinit var mediaPlayer: MediaPlayer
//
//    @Inject
//    lateinit var playerCommunication: PlayerCommunication
//
//    private lateinit var serviceComponent: PlayerServiceComponent
//
//    @Inject
//    lateinit var notificationManager: NotificationManagerCompat
//
//    @Inject
//    lateinit var tempTracksCache: TemporaryTracksCache
//
//    @Inject
//    lateinit var mediaSession: MediaSessionCompat
//
//    private var trackPosition = -1
//
//
//    override fun onCreate() {
//        isStarted = true
//        super.onCreate()
//       serviceComponent = (this.applicationContext as App).appComponent.playerServiceComponent().build()
//        serviceComponent.inject(this)
//
//    }
//
//    @SuppressLint("MissingPermission")
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        notificationManager.createNotificationChannel(notificationChannel)
//        startForeground(1, notificationBuilder.build())
//
//        notificationManager.notify(1,notificationBuilder.build())
//
//        lifecycleScope.launch(Dispatchers.IO) {
//            playerCommunication.collectPlayerServiceState(this@PlayerService){
//                try {
//                    //if(it is PlayerServiceState.Disabled) this@PlayerService.stopSelf()
//                    it.apply(
//                        mediaSession,
//                        mediaPlayer,
//                        notificationBuilder,
//                        notificationManager,
//                        this@PlayerService
//                    )
//                }catch (e:Exception){
//                    //playerCommunication.map(PlayerCommunicationState.Disabled)
//                }
//            }
//        }
//
//        lifecycleScope.launch {
//            playerCommunication.collectSelectedTrack(this@PlayerService){
//                it.apply{trackPosition = it}
//            }
//        }
//
//        lifecycleScope.launch(Dispatchers.IO) {
//            mediaPlayer.setOnCompletionListener {
//                val tracks = tempTracksCache.readTracks()
//                if (trackPosition == tracks.lastIndex)
//                    playerCommunication.map(
//                        PlayerCommunicationState.Play(
//                            tracks.first(), ItemPositionState.UpdateRecyclerViewSelectedItem(0)
//                        )
//                    )
//                else playerCommunication.map(
//                    PlayerCommunicationState.Play(
//                        tracks[trackPosition.inc()],
//                        ItemPositionState.UpdateRecyclerViewSelectedItem(trackPosition.inc())
//                    )
//                )
//            }
//        }
//        return super.onStartCommand(intent, flags, startId)
//    }
//
//    override fun onDestroy() {
//        isStarted = false
//        super.onDestroy()
//    }
//}

class PlayerService: MediaSessionService(){

    @Inject
    lateinit var player: ExoPlayer

    @Inject
    lateinit var mediaSession: MediaSession

    @Inject
    lateinit var communication: PlayerCommunication

    @SuppressLint("UnsafeOptInUsageError")
    override fun onCreate() {
        super.onCreate()
        Log.d("tag","----------------------------- MediaSessionService, onCreate")

        (this.applicationContext as App).appComponent
            .playerServiceComponent()
            .build()
            .inject(this)

    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession = mediaSession

    override fun onTaskRemoved(rootIntent: Intent?) {
        android.util.Log.d("tag", "onTaskRemoved: ")
        if (!player.playWhenReady) {
            stopSelf()
        }
    }
    //todo move tracks up trand frag

    override fun onDestroy() {
        player.release()
        mediaSession.release()
        super.onDestroy()
    }
}