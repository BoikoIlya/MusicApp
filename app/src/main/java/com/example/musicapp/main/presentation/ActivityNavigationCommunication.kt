package com.example.musicapp.main.presentation

import android.content.Intent
import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 20.06.2023.
 **/
interface ActivityNavigationCommunication: Communication.Mutable<ActivityNavigationState> {

    class Base @Inject constructor(): ActivityNavigationCommunication,
        Communication.UiUpdate<ActivityNavigationState>(ActivityNavigationState.Empty)
}