package com.example.musicapp.app.core

import android.util.Log
import com.example.musicapp.captcha.data.CaptchaRepository
import com.example.musicapp.captcha.data.RepeatActionAfterCaptcha
import com.example.musicapp.main.data.AuthorizationRepository
import javax.inject.Inject

/**
 * Created by HP on 16.05.2023.
 **/
interface HandleResponse {

    suspend fun <T> handle(
        block: suspend () -> T,
        error: suspend (String, Exception)-> T,
    ): T

    class Base @Inject constructor(
        private val auth: AuthorizationRepository,
        private val captchaRepository: CaptchaRepository,
        private val handleError: HandleError,
    ): HandleResponse{


        override suspend fun <T> handle(
            block: suspend () -> T,
            error: suspend (String, Exception) -> T,
        ): T =
            try {
               val result = block.invoke()
                captchaRepository.clearCaptcha()
                result
            }catch (e: Exception){
                when(e){
                    is UnAuthorizedException -> auth.logout()
                    is CaptchaNeededException-> e.map(Pair(captchaRepository,
                        object: RepeatActionAfterCaptcha {
                            override suspend fun invokeAction() {
                                handle(block,error)
                            }
                        }))

                }
                error.invoke(handleError.handle(e),e)

            }


    }

}