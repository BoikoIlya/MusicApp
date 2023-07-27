package com.example.musicapp.main.presentation

import android.content.Context
import android.content.Intent
import android.util.Log

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
            viewModel.releasePlayer()
            currentActivity.startActivity(Intent(currentActivity,activity))
            currentActivity.finish()
        }
    }
}