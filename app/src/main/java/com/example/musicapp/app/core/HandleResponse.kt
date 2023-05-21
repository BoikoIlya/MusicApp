package com.example.musicapp.app.core

import com.example.musicapp.main.data.AuthorizationRepository
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Created by HP on 16.05.2023.
 **/
interface HandleResponse<T> {

    suspend fun handle(
        block: suspend () -> T,
        error: suspend (String, Exception)-> T
    ): T

    class Base<T> @Inject constructor(
        private val auth: AuthorizationRepository,
        private val handleError: HandleError
    ): HandleResponse<T>{

        companion object{
            private const val unauthorized_response = 401
        }

        override suspend fun handle(
            block: suspend () -> T,
            error: suspend (String, Exception)-> T
            ): T =
                try {
                     block.invoke()
                }catch (e: HttpException){
                    if(e.code() == unauthorized_response)
                    {
                        try {
                            auth.updateToken()
                        }catch (e:Exception){
                            error.invoke(handleError.handle(e),e)
                        }
                        handle(block,error)
                    }else error.invoke(handleError.handle(e),e)
                }catch (e: Exception) {
                    error.invoke(handleError.handle(e),e)
                }


    }

}