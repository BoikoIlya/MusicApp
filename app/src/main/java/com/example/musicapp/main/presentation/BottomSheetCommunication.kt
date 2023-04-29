package com.example.musicapp.main.presentation

import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.example.musicapp.app.core.Communication
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

/**
 * Created by HP on 22.04.2023.
 **/
interface BottomSheetCommunication: Communication.Mutable<Int> {

    class Base @Inject constructor(): BottomSheetCommunication, Communication.UiUpdate<Int>(BottomSheetBehavior.STATE_COLLAPSED)

}