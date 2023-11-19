package com.kamancho.melisma.main.presentation

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.kamancho.melisma.update.UpdateManager

/**
 * Created by HP on 05.07.2023.
 **/
sealed interface ActivityNavigationState{

    fun apply(
        currentActivity: MainActivity,
        viewModel: MainViewModel,
    )

    fun apply(
        resultLauncher: ActivityResultLauncher<IntentSenderRequest>,
        updateListener: InstallStateUpdatedListener,
        updateManager: UpdateManager
    )

    object Empty: ActivityNavigationState {
        override fun apply(currentActivity: MainActivity,viewModel:MainViewModel) = Unit
        override fun apply(
            resultLauncher: ActivityResultLauncher<IntentSenderRequest>,
            updateListener: InstallStateUpdatedListener,
            updateManager: UpdateManager
        ) = Unit
    }

    object LoggedIn: ActivityNavigationState {
        override fun apply(currentActivity: MainActivity, viewModel: MainViewModel) = Unit

        override fun apply(
            resultLauncher: ActivityResultLauncher<IntentSenderRequest>,
            updateListener: InstallStateUpdatedListener,
            updateManager: UpdateManager
        ) {
            updateManager.checkForUpdate(resultLauncher,updateListener)
        }
    }

    data class Navigate<T>(
        private val activity: Class<T>
    ): ActivityNavigationState {

        override fun apply(currentActivity: MainActivity,viewModel:MainViewModel) {
            viewModel.clearDisablePlayer()
            currentActivity.startActivity(Intent(currentActivity,activity))
            currentActivity.finish()
        }

        override fun apply(
            resultLauncher: ActivityResultLauncher<IntentSenderRequest>,
            updateListener: InstallStateUpdatedListener,
            updateManager: UpdateManager
        ) = Unit
    }
}