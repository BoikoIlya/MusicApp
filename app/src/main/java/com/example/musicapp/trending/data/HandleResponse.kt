package com.example.musicapp.trending.data

import android.util.Log
import com.example.musicapp.core.NoInternetConnectionException
import com.example.musicapp.core.ServiceUnavailableException
import retrofit2.Response
import java.net.UnknownHostException
import java.net.UnknownServiceException

interface HandleResponse {

   suspend fun <T>handle(
        block: suspend ()->T
    ):T

    class Base: HandleResponse{

        override suspend fun <T> handle(block: suspend () -> T): T {
            return try {
                val result = block.invoke()
                result
            }catch (e:Exception){
                throw when(e){
                    is UnknownHostException -> NoInternetConnectionException()
                    else -> ServiceUnavailableException()
                }

            }
        }
    }
}
