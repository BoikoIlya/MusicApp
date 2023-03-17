package com.example.musicapp.trending.data

import android.util.Log
import com.example.musicapp.app.core.ServiceUnavailableException
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

interface HandleResponse {

   suspend fun <T>handle(
        block: suspend ()->T
    ):T
   //todo maybe should remove class
    class Base @Inject constructor(): HandleResponse{

        override suspend fun <T> handle(block: suspend () -> T): T {
            return try {
                val result = block.invoke()
                result
            }catch (e:Exception){
                Log.d("tag", "handle: ${e.message}")
                throw when(e){
                    is UnknownHostException -> e
                    is HttpException -> e
                    else -> ServiceUnavailableException()
                }

            }
        }
    }
}
