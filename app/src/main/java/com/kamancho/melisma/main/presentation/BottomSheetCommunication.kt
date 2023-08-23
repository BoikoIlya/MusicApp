package com.kamancho.melisma.main.presentation

import com.kamancho.melisma.app.core.Communication
import com.google.android.material.bottomsheet.BottomSheetBehavior
import javax.inject.Inject

/**
 * Created by HP on 22.04.2023.
 **/
interface BottomSheetCommunication: Communication.Mutable<Int> {

    class Base @Inject constructor(): BottomSheetCommunication, Communication.UiUpdate<Int>(BottomSheetBehavior.STATE_COLLAPSED)

}