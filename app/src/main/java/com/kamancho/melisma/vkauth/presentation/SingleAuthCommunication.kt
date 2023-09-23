package com.kamancho.melisma.vkauth.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 15.09.2023.
 **/
interface SingleAuthCommunication: Communication.MutableSingle<SingleAuthState> {

    object Base : SingleAuthCommunication, Communication.SingleUiUpdate<SingleAuthState>()
}