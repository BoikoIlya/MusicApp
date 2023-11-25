package com.kamancho.melisma.main.presentation

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * Created by HP on 28.04.2023.
 **/
interface FirebaseInitializer {

    fun init()

    class Base (
        private val topic: String
    ): FirebaseInitializer{


        override  fun init() {
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
        }
    }

}