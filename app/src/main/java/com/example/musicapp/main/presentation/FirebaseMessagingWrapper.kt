package com.example.musicapp.main.presentation

import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject

/**
 * Created by HP on 28.04.2023.
 **/
interface FirebaseMessagingWrapper {

    fun subscribeToTopic()

    class Base @Inject constructor(
        private val firebaseMessaging: FirebaseMessaging,
        private val topic: String
    ): FirebaseMessagingWrapper{


        override fun subscribeToTopic() {
            firebaseMessaging.subscribeToTopic(topic)
        }
    }

}