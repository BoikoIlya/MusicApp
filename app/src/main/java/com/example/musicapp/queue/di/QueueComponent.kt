package com.example.musicapp.queue.di

import com.example.musicapp.player.di.BindsPlayerModule
import com.example.musicapp.player.di.PlayerComponent
import com.example.musicapp.player.di.PlayerModule
import com.example.musicapp.player.di.PlayerServiceScope
import com.example.musicapp.player.presentation.PlayerFragment
import com.example.musicapp.player.presentation.PlayerService
import com.example.musicapp.queue.presenatation.QueueFragment
import dagger.Subcomponent

/**
 * Created by HP on 23.04.2023.
 **/
@QueueScope
@Subcomponent(modules = [QueueModule::class])
interface QueueComponent {

    @Subcomponent.Builder
    interface Builder{
        fun build(): QueueComponent
    }

    fun inject(queueFragment: QueueFragment)
}

