package com.kamancho.melisma.main.presentation

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import com.kamancho.melisma.app.core.SDKChecker
import com.kamancho.melisma.app.core.SDKCheckerState

/**
 * Created by Ilya Boiko @camancho
on 10.10.2023.
 **/
interface PermissionsChain {

  fun check(sdkChecker: SDKChecker,permissions: MutableList<String>):List<String>

  class NotificationPermission(
      private val nextChainItem: PermissionsChain
  ): PermissionsChain{

   @RequiresApi(Build.VERSION_CODES.TIRAMISU)
   override fun check(sdkChecker: SDKChecker, permissions: MutableList<String>):List<String> {

    sdkChecker.check(SDKCheckerState.AboveApi32,{
     permissions.add(Manifest.permission.POST_NOTIFICATIONS)
    },{})
       return nextChainItem.check(sdkChecker, permissions)
   }
  }

    class WriteExternalStoragePermission(
        private val nextChainItem: PermissionsChain
    ): PermissionsChain {

        override fun check(sdkChecker: SDKChecker, permissions: MutableList<String>): List<String> {
           permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
           return nextChainItem.check(sdkChecker, permissions)
        }
    }

    class LastChainItem: PermissionsChain {
        override fun check(sdkChecker: SDKChecker, permissions: MutableList<String>): List<String>
        = permissions
    }
}