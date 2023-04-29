package com.example.musicapp.main.data

import com.example.musicapp.main.data.cloud.AuthorizationService
import javax.inject.Inject
import android.util.Base64
import com.example.musicapp.main.data.cache.TokenStore

/**
 * Created by HP on 09.03.2023.
 **/
interface AuthorizationRepository {

   suspend fun updateToken()


    class Base @Inject constructor(
        private val authorizationService: AuthorizationService,
        private val cache: TokenStore
    ): AuthorizationRepository {

        companion object{
            private const val client_id = "60c4c91a06044b06b6db1066699eeb13"
            private const val client_secret = "487d1386b22243c18ce3a90945f2b944"
            private val auth = "Basic "+ Base64.encodeToString(("$client_id:$client_secret").toByteArray(), Base64.NO_WRAP)
            private const val content = "application/x-www-form-urlencoded"
            private const val grand_type = "client_credentials"
        }



        override suspend fun updateToken() {
            authorizationService.getToken(auth, content, grand_type).map(cache)
        }

    }
}