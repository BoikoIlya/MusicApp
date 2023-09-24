package com.kamancho.melisma.main.data.cloud

import android.util.Log
import androidx.annotation.Keep
import com.google.gson.JsonParser
import com.kamancho.melisma.app.core.AuthRedirectException
import com.kamancho.melisma.app.core.CaptchaNeededException
import com.kamancho.melisma.app.core.ExtendedGsonConverterFactory
import com.kamancho.melisma.app.core.InvalidAuthorizationData
import com.kamancho.melisma.app.core.ServiceUnavailableException
import com.kamancho.melisma.app.core.SomethingWentWrongTryLater
import com.kamancho.melisma.app.core.UnAuthorizedException
import com.kamancho.melisma.app.core.VkException
import com.kamancho.melisma.app.vkdto.TokenDto
import com.kamancho.melisma.captcha.data.cache.CaptchaDataStore
import javax.inject.Inject

/**
 * Created by HP on 17.09.2023.
 **/

interface AuthorizationCloudDataSource {

    suspend fun token(login: String, password: String): TokenDto
    @Keep
    class Base @Inject constructor(
        private val authorizationService: AuthorizationService,
        private val captchaDataStore: CaptchaDataStore
    ): AuthorizationCloudDataSource {

        companion object{
            private const val invalid_client = "invalid_client"
            private const val need_validation = "need_validation"
            private const val need_captcha = "need_captcha"
            private const val error_field = "error"
            private const val redirect_uri_field = "redirect_uri"
            private const val captcha_sid_field = "captcha_sid"
            private const val captcha_img_field = "captcha_img"
        }

        override suspend fun token(login: String, password: String): TokenDto {

            val response = authorizationService.getToken(
                username = login,
                password = password,
                captcha_key = captchaDataStore.captchaId(),
                captcha_sid = captchaDataStore.captchaEnteredData()
            )

            if(response.isSuccessful && response.body()!=null){
               return response.body()!!
            }else {
                val jsonString = response.errorBody()?.string()
                val json = JsonParser.parseString(jsonString).asJsonObject
                val error = json.get(error_field).asString

                throw when(error){
                    invalid_client -> throw InvalidAuthorizationData()
                    need_validation-> throw AuthRedirectException(json.get(redirect_uri_field).toString())
                    need_captcha-> throw CaptchaNeededException(
                        json.get(captcha_sid_field).asString,
                        json.get(captcha_img_field).asString
                    )
                    else -> {
                        val regex = Regex("\\d+") // Match one or more digits
                        val matchResult = regex.find(error)
                        val errorCode =  matchResult?.value?.toIntOrNull()

                        if(errorCode!=null) VkException.Base(errorCode)
                        else SomethingWentWrongTryLater()
                    }
                }

            }
        }
    }
}