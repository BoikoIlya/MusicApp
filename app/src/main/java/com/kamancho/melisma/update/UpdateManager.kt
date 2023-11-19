package com.kamancho.melisma.update

import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

/**
 * Created by Ilya Boiko @camancho
on 11.11.2023.
 **/
interface UpdateManager {

    fun checkForUpdate(
     activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>,
     listener: InstallStateUpdatedListener,
    )

    fun unregisterListener(listener: InstallStateUpdatedListener)

    fun completeUpdate()

    class Base constructor(
        private val appUpdateManager: AppUpdateManager,
    ) : UpdateManager {

        private var appUpdateInfoTask: Task<AppUpdateInfo>? = null

        override fun checkForUpdate(
            activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>,
            listener: InstallStateUpdatedListener,
        ) {

            if (appUpdateInfoTask!=null) return

            appUpdateInfoTask = appUpdateManager.appUpdateInfo

            appUpdateInfoTask?.addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
                ) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        activityResultLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
                    )

                }
            }
            appUpdateManager.registerListener(listener)

        }

        override fun unregisterListener(listener: InstallStateUpdatedListener) =
            appUpdateManager.unregisterListener(listener)

        override fun completeUpdate() {
            appUpdateManager.completeUpdate()
        }

    }
}