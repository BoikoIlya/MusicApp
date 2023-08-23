package com.example.musicapp.app.core

import android.os.Build
import javax.inject.Inject

/**
 * Created by HP on 11.07.2023.
 **/
interface SDKChecker {

    fun check(
        state: SDKCheckerState,
        positive: ()->Unit,
        negative:()->Unit
        )

    class Base @Inject constructor(): SDKChecker{

        override fun check(
            state: SDKCheckerState,
            positive:()->Unit,
            negative:()->Unit
        ) {
            if(state.apply()) positive.invoke()
            else negative.invoke()
        }

    }
}

interface SDKCheckerState{

    fun apply(): Boolean

    object AboveApi32: SDKCheckerState{
        override fun apply(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    object BelowApi29: SDKCheckerState{
        override fun apply(): Boolean = Build.VERSION.SDK_INT<=Build.VERSION_CODES.P
    }
}