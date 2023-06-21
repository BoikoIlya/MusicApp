package com.example.musicapp.main.data

import com.example.musicapp.main.data.cloud.AuthorizationService
import javax.inject.Inject
import com.example.musicapp.app.core.HandleError
import com.example.musicapp.main.data.cache.AccountDataStore

/**
 * Created by HP on 09.03.2023.
 **/
interface AuthorizationRepository: CheckAuthRepository {

   suspend fun updateToken()

   suspend fun token(login: String, password: String): String



    class Base @Inject constructor(
        private val authorizationService: AuthorizationService,
        private val accountData: AccountDataStore,
        private val handleError: HandleError
    ): AuthorizationRepository {





        override suspend fun updateToken() {
          //  authorizationService.getToken(auth, content, grand_type).map(cache)
        }

        override suspend fun token(login: String, password: String): String {
            return try {
                authorizationService.getToken(username = login, password = password).map(accountData)
                ""
            }catch (e: Exception){
                handleError.handle(e)
            }
        }

        override suspend fun isAuthorized(notAuthorized: () -> Unit) {
            if(accountData.isDataEmpty()) notAuthorized.invoke()
        }

    }
}