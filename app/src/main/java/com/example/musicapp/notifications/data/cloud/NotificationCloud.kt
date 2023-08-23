package com.example.musicapp.notifications.data.cloud

import android.os.Parcelable
import com.example.musicapp.BuildConfig
import com.example.musicapp.app.core.Mapper
import com.example.musicapp.notifications.domain.NotificationDomain
import kotlinx.parcelize.Parcelize

/**
 * Created by HP on 21.08.2023.
 **/
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