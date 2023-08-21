package com.example.musicapp.player.di

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.media.AudioFocusRequest
import android.media.AudioManager
import androidx.lifecycle.ViewModel
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.HttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.DefaultHlsDataSourceFactory
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.session.MediaSession
import com.example.musicapp.main.di.ViewModelKey
import com.example.musicapp.main.presentation.ControllerListener
import com.example.musicapp.main.presentation.MainActivity
import com.example.musicapp.main.presentation.SlideViewPagerCommunication
import com.example.musicapp.player.presentation.AudioFocusChangeListener
import com.example.musicapp.player.presentation.DeleteTrackFromPlayerMenuDialogViewModel
import com.example.musicapp.player.presentation.MediaSessionCallBack
import com.example.musicapp.player.presentation.PlayerViewModel
import com.example.musicapp.player.presentation.TrackPlaybackPositionCommunication
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.multibindings.IntoMap
import javax.inject.Singleton


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
        cacheDataSourceFactory: CacheDataSource.Factory
    ): ExoPlayer {
//        return ExoPlayer.Builder(context)
//            .setMediaSourceFactory(
//                HlsMediaSource.Factory(cacheDataSourceFactory))
//            .build().also { it.addListener(listener) }
        return ExoPlayer.Builder(context)
            .setMediaSourceFactory(
                HlsMediaSource.Factory(DefaultHttpDataSource.Factory()))
            .build().also { it.addListener(listener) }
//        return ExoPlayer.Builder(context)
//            .setMediaSourceFactory(
//                HlsMediaSource.Factory(DefaultHlsDataSourceFactory(DefaultHttpDataSource.Factory())))
//            .build().also { it.addListener(listener) } //BEFORE

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
    fun provideAudioFocusRequest(audioFocusChangeListener: AudioFocusChangeListener): AudioFocusRequest {
        return AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setAudioAttributes(
                android.media.AudioAttributes.Builder()
                    .setUsage(android.media.AudioAttributes.USAGE_GAME)
                    .setContentType(android.media.AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build())
            .setAcceptsDelayedFocusGain(true)
            .setOnAudioFocusChangeListener(audioFocusChangeListener)
            .build()
    }

}

@Module
interface BindsPlayerModule{


    @Binds
    @PlayerServiceScope
    fun bindControllerListener(obj: ControllerListener.Base): ControllerListener

    @Binds
    @[IntoMap ViewModelKey(PlayerViewModel::class)]
    fun bindPlayerViewModel(playerViewModel: PlayerViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(DeleteTrackFromPlayerMenuDialogViewModel::class)]
    fun bindDeleteTrackFromPlayerMenuDialogViewModel(deleteTrackFromPlayerMenuDialogViewModel: DeleteTrackFromPlayerMenuDialogViewModel): ViewModel




}