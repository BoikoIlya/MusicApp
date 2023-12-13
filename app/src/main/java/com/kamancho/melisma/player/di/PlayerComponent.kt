package com.kamancho.melisma.player.di

import com.kamancho.melisma.artisttracks.di.ArtistsTracksComponent
import com.kamancho.melisma.player.presentation.DeleteTrackFromPlayerMenuDialog
import com.kamancho.melisma.player.presentation.HeadPhonesReceiver
import com.kamancho.melisma.player.presentation.PlayerFragment
import com.kamancho.melisma.player.presentation.PlayerService
import com.kamancho.melisma.queue.di.QueueModule
import com.kamancho.melisma.queue.presenatation.QueueFragment
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

    fun artistsTracksComponent(): ArtistsTracksComponent.Builder
}