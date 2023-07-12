package com.example.musicapp.app.core

import android.util.Log
import com.example.musicapp.main.data.AuthorizationRepository
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Created by HP on 16.05.2023.
 **/
interface HandleResponse {

    suspend fun <T> handle(
        block: suspend () -> T,
        error: suspend (String, Exception)-> T
    ): T

    class Base @Inject constructor(
        private val auth: AuthorizationRepository,
        private val handleError: HandleError,
    ): HandleResponse{


        override suspend fun <T> handle(
            block: suspend () -> T,
            error: suspend (String, Exception) -> T,
        ): T =
            try {
                block.invoke()
            }catch (e: Exception){
                if(e is UnAuthorizedException)
                    auth.clearData()
                error.invoke(handleError.handle(e),e)
            }


    }

}