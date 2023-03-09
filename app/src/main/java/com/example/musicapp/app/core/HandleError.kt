package com.example.musicapp.app.core

import com.example.musicapp.R
import javax.inject.Inject

/**
 * Created by HP on 26.01.2023.
 **/
interface HandleError {

    fun handle(e: Exception): String

    class Base @Inject constructor(
        private val managerResource: ManagerResource
    ): HandleError {

        override fun handle(e: Exception): String {
            return managerResource.getString(
                when(e){
                    is NoInternetConnectionException -> R.string.no_connection_message
                    is NoSuchElementException -> R.string.no_element
                    else -> R.string.service_is_unavailable
                }
            )
        }

    }
}