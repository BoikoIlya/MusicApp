package com.kamancho.melisma.main.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 11.07.2023.
 **/
interface PermissionCheckCommunication : Communication.Mutable<PermissionCheckState>  {

    class Base @Inject constructor(): PermissionCheckCommunication,Communication.UiUpdate<PermissionCheckState>(PermissionCheckState.Empty)
}