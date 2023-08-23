package com.kamancho.melisma.main.presentation

import android.content.Intent

/**
 * Created by HP on 05.07.2023.
 **/
sealed interface ActivityNavigationState{

    fun apply(currentActivity: MainActivity,viewModel:MainViewModel)

    object Empty: ActivityNavigationState {
        override fun apply(currentActivity: MainActivity,viewModel:MainViewModel) = Unit
    }

    data class Navigate<T>(
        private val activity: Class<T>
    ): ActivityNavigationState {

        override fun apply(currentActivity: MainActivity,viewModel:MainViewModel) {
            viewModel.clearDisablePlayer()
            currentActivity.startActivity(Intent(currentActivity,activity))
            currentActivity.finish()
        }
    }
}