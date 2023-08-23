package com.kamancho.melisma.creteplaylist.presentation

import com.kamancho.melisma.addtoplaylist.presentation.SelectedTrackUi
import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 16.07.2023.
 **/
interface SelectedTracksCommunication: Communication.Mutable<List<SelectedTrackUi>> {

    class Base @Inject constructor() : SelectedTracksCommunication,Communication.UiUpdate<List<SelectedTrackUi>>(emptyList())

}