package com.kamancho.melisma.notifications.domain

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.ManagerResource
import com.kamancho.melisma.notifications.presentation.NotificationUi
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Created by HP on 21.08.2023.
 **/
data class NotificationDomain(
    private val id: String,
    private val mainText: String,
    private val titleText: String,
    private val date: String,
    private val notificationType: Int,
    private val btnText: String,
    private val redirectUrl: String
){

    companion object{
        const val TYPE_REVIEW = 0
        const val TYPE_PERMISSION = 1
        const val TYPE_REDIRECTION = 2
        const val TYPE_DEFAULT = 3
    }


    fun map(managerResource: ManagerResource): NotificationUi{
       return when(notificationType){
            TYPE_DEFAULT -> NotificationUi.DefaultNotification(title = titleText, mainText = mainText.replace("\\n","\n"), date = date, id = id)
            TYPE_PERMISSION-> {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.data = Uri.fromParts("package", managerResource.getPackageName(), null)
                NotificationUi.PrimaryNotification(
                    title = titleText,
                    mainText = mainText.replace("\\n","\n"),
                    btnText = btnText,
                    stokeColor = managerResource.getColor(R.color.red),
                    strokeWidth = managerResource.getDimensionPixelSize(R.dimen.notification_stroke_width),
                    intent = intent,
                    colorStateList = managerResource.getColorStateList(R.color.red),
                    id = id
                )
            }
           TYPE_REDIRECTION->{
               val intent = Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl))
               NotificationUi.PrimaryNotification(
                   title = titleText,
                   mainText = mainText.replace("\\n","\n"),
                   btnText = btnText,
                   stokeColor = managerResource.getColor(R.color.orange),
                   strokeWidth = managerResource.getDimensionPixelSize(R.dimen.notification_stroke_width),
                   intent = intent,
                   colorStateList = managerResource.getColorStateList(R.color.orange),
                   id = id
               )
           }
           TYPE_REVIEW->NotificationUi.ReviewNotification(
               id = "0",
               title = titleText,
               mainText = mainText.replace("\\n","\n"),
               btnText = btnText,
               stokeColor = managerResource.getColor(R.color.orange),
               strokeWidth = managerResource.getDimensionPixelSize(R.dimen.notification_stroke_width),
               colorStateList = managerResource.getColorStateList(R.color.orange)
           )
           else -> throw Exception()
        }
    }

    fun notificationType() = notificationType

    fun dateFormatted() = SimpleDateFormat("dd.MM.yyyy", Locale.US).parse(date)
}
