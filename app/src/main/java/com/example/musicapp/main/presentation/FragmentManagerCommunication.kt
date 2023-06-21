package com.example.musicapp.main.presentation

import androidx.fragment.app.Fragment
import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 20.06.2023.
 **/
interface FragmentManagerCommunication: Communication.Mutable<FragmentManagerState> {

    class Base @Inject constructor(): FragmentManagerCommunication,Communication.UiUpdate<FragmentManagerState>(FragmentManagerState.Empty)
}