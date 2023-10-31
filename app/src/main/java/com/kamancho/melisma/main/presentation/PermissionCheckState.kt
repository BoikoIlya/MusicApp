package com.kamancho.melisma.main.presentation

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Created by HP on 11.07.2023.
 **/
sealed interface PermissionCheckState{

    fun apply(context: Context)

    data class CheckForPermissions(
        private val permissions: List<String>,
        private val permissionRequestCode: Int,
    ): PermissionCheckState{

        override fun apply(context: Context) {
            val notGranted = mutableListOf<String>()
            permissions.forEach{
                if(ContextCompat.checkSelfPermission(context,it) != PackageManager.PERMISSION_GRANTED)
                    notGranted.add(it)
            }
            ActivityCompat.requestPermissions(context as MainActivity, permissions.toTypedArray(),permissionRequestCode)
        }
    }

    object Empty: PermissionCheckState {
        override fun apply(context: Context) = Unit
    }
}