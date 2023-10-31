package com.kamancho.melisma.search.presentation

import com.kamancho.melisma.app.core.Communication
import com.kamancho.melisma.app.core.PagingLoadState
import javax.inject.Inject

/**
 * Created by Ilya Boiko @camancho
on 26.10.2023.
 **/

    interface SearchPagingLoadStateCommunicationTracks : Communication.Mutable<PagingLoadState>{
        class Base @Inject constructor(): SearchPagingLoadStateCommunicationTracks,
            Communication.UiUpdate<PagingLoadState>(PagingLoadState.Empty)
    }

    interface SearchPagingLoadStateCommunicationPlaylists : Communication.Mutable<PagingLoadState>{
        class Base @Inject constructor(): SearchPagingLoadStateCommunicationPlaylists,
            Communication.UiUpdate<PagingLoadState>(PagingLoadState.Empty)
    }
