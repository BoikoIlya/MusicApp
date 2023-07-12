package com.example.musicapp.app.core

/**
 * Created by HP on 08.07.2023.
 **/
interface UnAuthorizedResponseHandler<T> {

    fun handle(): T
}