package com.kamancho.melisma.artisttracks.di

import com.kamancho.melisma.addtoplaylist.di.AddToPlaylistComponent
import com.kamancho.melisma.artisttracks.presentation.ArtistTracksDialogFragmentBottomSheet
import com.kamancho.melisma.artisttracks.presentation.ArtistTracksListFragment
import com.kamancho.melisma.creteplaylist.presentation.CreatePlaylistFragment
import com.kamancho.melisma.editplaylist.presentation.EditPlaylistFragment
import com.kamancho.melisma.searchplaylistdetails.di.SearchPlaylistDetailsComponent
import dagger.Subcomponent

/**
 * Created by HP on 21.03.2023.
 **/
@ArtistsTracksScope
@Subcomponent(modules = [ArtistsTracksModule::class, ArtistsTracksModuleProvides::class])
interface ArtistsTracksComponent {

    @Subcomponent.Builder
    interface Builder{
        fun build(): ArtistsTracksComponent
    }

    fun inject(artistsTracksListFragment: ArtistTracksListFragment)
    fun inject(artistTracksDialogFragmentBottomSheet: ArtistTracksDialogFragmentBottomSheet)


}