package com.example.musicapp.core

import com.example.musicapp.R

/**
 * Created by HP on 26.01.2023.
 **/
interface HandleError {

    fun handle(e: Exception): String

    class Base(
        private val managerResource: ManagerResource
    ): HandleError{

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