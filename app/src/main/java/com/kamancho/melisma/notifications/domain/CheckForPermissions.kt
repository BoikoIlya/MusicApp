package com.kamancho.melisma.notifications.domain

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.kamancho.melisma.R
import javax.inject.Inject

/**
 * Created by HP on 21.08.2023.
 **/
interface CheckForPermissions {

    fun check(): List<NotificationDomain>

    class Base @Inject constructor(
        private val context: Context
    ):CheckForPermissions {

        override fun check(): List<NotificationDomain> {
            val notificationsList = emptyList<NotificationDomain>().toMutableList()
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                )
                    notificationsList.add(
                        NotificationDomain(
                            mainText = context.getString(R.string.please_give_notification_permission_to_make_function_app_properly),
                            titleText = context.getString(R.string.please_give_permission),
                            date = "",
                            notificationType = NotificationDomain.TYPE_PERMISSION,
                            btnText = context.getString(R.string.go_to_settings),
                            redirectUrl = "",
                            id = ""
                        )
                    )
            }
            else if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                )
                    notificationsList.add(
                        NotificationDomain(
                            mainText = context.getString(R.string.give_storage_permission_to_be_able_download),
                            titleText = context.getString(R.string.please_give_permission),
                            date = "",
                            notificationType = NotificationDomain.TYPE_PERMISSION,
                            btnText = context.getString(R.string.go_to_settings),
                            redirectUrl = "",
                            id = ""
                        )
                    )
            }
            return notificationsList
        }

    }
}