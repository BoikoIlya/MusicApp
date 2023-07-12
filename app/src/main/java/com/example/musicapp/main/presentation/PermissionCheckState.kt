package com.example.musicapp.main.presentation

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Created by HP on 11.07.2023.
 **/
sealed interface PermissionCheckState{

    fun apply(context: Context)

    data class CheckForPermission(
        private val permission: String,
        private val permissionRequestCode: Int,
    ): PermissionCheckState{

        override fun apply(context: Context) {
            if(ContextCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(context as MainActivity, arrayOf(permission),permissionRequestCode)
        }
    }

    object Empty: PermissionCheckState {
        override fun apply(context: Context) = Unit
    }
}