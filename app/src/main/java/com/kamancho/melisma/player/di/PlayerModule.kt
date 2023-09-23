package com.kamancho.melisma.player.di

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.util.EventLogger
import androidx.media3.session.MediaSession
import com.kamancho.melisma.main.di.ViewModelKey
import com.kamancho.melisma.main.presentation.ControllerListener
import com.kamancho.melisma.main.presentation.MainActivity
import com.kamancho.melisma.player.presentation.AudioFocusChange
import com.kamancho.melisma.player.presentation.AudioFocusChangeListener
import com.kamancho.melisma.player.presentation.DeleteTrackFromPlayerMenuDialogViewModel
import com.kamancho.melisma.player.presentation.MediaSessionCallBack
import com.kamancho.melisma.player.presentation.PlayerViewModel
import com.kamancho.melisma.player.presentation.SleepTimer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap


@UnstableApi /**
 * Created by HP on 30.01.2023.
 **/
@Module
class PlayerModule {



    @Provides
    @PlayerServiceScope
    fun provideMediaPlayer(
        context: Context,
        listener: ControllerListener,
    ): ExoPlayer {
        return ExoPlayer.Builder(context)
            .build().also {
                it.addListener(listener)
                it.addAnalyticsListener(EventLogger())
            }
    }



    @Provides
    @PlayerServiceScope
    fun providePendingIntent(context: Context): PendingIntent {
        return TaskStackBuilder.create(context).run {
            addNextIntent(Intent(context, MainActivity::class.java))
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    @Provides
    @PlayerServiceScope
    fun provideMediaSession(
        context: Context,
        player: ExoPlayer,
        pendingIntent: PendingIntent,
        callBack: MediaSessionCallBack,
    ): MediaSession {
        return MediaSession.Builder(context, player)
            .setSessionActivity(pendingIntent)
            .setCallback(callBack)
            .build()
    }



    @Provides
    @PlayerServiceScope
    fun provideAudioManager(context: Context): AudioManager{
        return context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    @Provides
    @PlayerServiceScope
    fun provideAudioFocusRequest(
        audioFocusChangeListener: AudioFocusChangeListener,
        audioManager: AudioManager
    ): AudioFocusChange {

        return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1)
            AudioFocusChange.BelowApi26(audioFocusChangeListener, audioManager)
            else {
            AudioFocusChange.AboveApi25(
                audioManager,
                AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(
                        android.media.AudioAttributes.Builder()
                            .setUsage(android.media.AudioAttributes.USAGE_GAME)
                            .setContentType(android.media.AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAcceptsDelayedFocusGain(true)
                    .setOnAudioFocusChangeListener(audioFocusChangeListener)
                    .build()
            )
        }
    }

}

@Module
interface BindsPlayerModule{


    @Binds
    @PlayerServiceScope
    fun bindControllerListener(obj: ControllerListener.Base): ControllerListener

    @Binds
    @PlayerServiceScope
    fun bindSleepTimer(obj: SleepTimer.Base): SleepTimer

    @Binds
    @[IntoMap ViewModelKey(PlayerViewModel::class)]
    fun bindPlayerViewModel(playerViewModel: PlayerViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(DeleteTrackFromPlayerMenuDialogViewModel::class)]
    fun bindDeleteTrackFromPlayerMenuDialogViewModel(deleteTrackFromPlayerMenuDialogViewModel: DeleteTrackFromPlayerMenuDialogViewModel): ViewModel




}