package com.kamancho.melisma.addtoplaylist.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 16.07.2023.
 **/
interface CachedTracksCommunication: Communication.Mutable<List<SelectedTrackUi>> {

    class Base @Inject constructor(): CachedTracksCommunication,Communication.UiUpdate<List<SelectedTrackUi>>(
        emptyList())
}