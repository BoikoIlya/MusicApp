package com.example.musicapp.player.di

import com.example.musicapp.player.presentation.DeleteTrackFromPlayerMenuDialog
import com.example.musicapp.player.presentation.HeadPhonesReceiver
import com.example.musicapp.player.presentation.PlayerFragment
import com.example.musicapp.player.presentation.PlayerService
import com.example.musicapp.queue.di.QueueModule
import com.example.musicapp.queue.presenatation.QueueFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.Subcomponent

/**
 * Created by HP on 30.01.2023.
 **/
@PlayerServiceScope
@Subcomponent(modules = [PlayerModule::class, BindsPlayerModule::class, QueueModule::class])
interface PlayerComponent {

    @Subcomponent.Builder
    interface Builder{
        fun build(): PlayerComponent
    }

    fun inject(playerFragment: PlayerFragment)
    fun inject(playerService: PlayerService)
    fun inject(headPhonesReceiver: HeadPhonesReceiver)

    fun inject(queueFragment: QueueFragment)
    fun inject(deleteTrackFromPlayerMenuDialog: DeleteTrackFromPlayerMenuDialog)
}