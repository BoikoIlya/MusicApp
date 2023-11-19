package com.kamancho.melisma.favoritesplaylistdetails.presentation

import android.view.View
import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface AddBtnVisibilityCommunication: Communication.Mutable<Int>{
    class Base @Inject constructor():
        Communication.UiUpdate<Int>(View.GONE),
        AddBtnVisibilityCommunication
}