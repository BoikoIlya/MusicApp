package com.kamancho.melisma.notifications.data.cloud

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import com.kamancho.melisma.app.core.Mapper
import com.kamancho.melisma.notifications.domain.NotificationDomain
import kotlinx.parcelize.Parcelize

/**
 * Created by HP on 21.08.2023.
 **/
@Keep
@Parcelize
data class NotificationCloud(
    val id: String,
    val mainText: String,
    val title: String,
    val date: String,
    val notificationType: Int,
    val buttonText: String,
    val redirectUrl: String,
) : Parcelable, Mapper<Unit, NotificationDomain> {

    constructor(): this("","", "", "",0,"","")


    override fun map(data: Unit): NotificationDomain {
       return  NotificationDomain(
           mainText = mainText,
           titleText = title,
           date = date,
           notificationType = notificationType,
           btnText = buttonText,
           redirectUrl = redirectUrl,
           id = id
       )
    }
}