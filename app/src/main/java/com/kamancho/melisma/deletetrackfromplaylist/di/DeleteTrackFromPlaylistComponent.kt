package com.kamancho.melisma.deletetrackfromplaylist.di

import com.kamancho.melisma.deletetrackfromplaylist.presentation.DeleteTrackFromPlaylistDialogFragment
import dagger.Subcomponent

/**
 * Created by HP on 28.01.2023.
 **/
@DeleteTrackFromPlaylistScope
@Subcomponent(modules = [DeleteTrackFromPlaylistModule::class,DeleteTrackFromPlaylistModuleProvides::class])
interface DeleteTrackFromPlaylistComponent {

    @Subcomponent.Builder
    interface Builder{
       fun build(): DeleteTrackFromPlaylistComponent
    }


    fun inject(deleteTrackFromPlaylistDialogFragment: DeleteTrackFromPlaylistDialogFragment)

}