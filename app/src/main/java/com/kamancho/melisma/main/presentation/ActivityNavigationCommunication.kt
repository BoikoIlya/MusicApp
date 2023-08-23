package com.kamancho.melisma.main.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 20.06.2023.
 **/
interface ActivityNavigationCommunication: Communication.Mutable<ActivityNavigationState> {

    class Base @Inject constructor(): ActivityNavigationCommunication,
        Communication.UiUpdate<ActivityNavigationState>(ActivityNavigationState.Empty)
}