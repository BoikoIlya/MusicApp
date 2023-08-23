package com.kamancho.melisma.main.data

import com.kamancho.melisma.main.data.cloud.AuthorizationService
import javax.inject.Inject
import com.kamancho.melisma.app.core.HandleError
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.app.core.MusicDBManager
import com.kamancho.melisma.main.data.cache.AccountDataStore
import kotlinx.coroutines.flow.Flow

/**
 * Created by HP on 09.03.2023.
 **/
interface AuthorizationRepository: CheckAuthRepository {

   suspend fun logout()

   suspend fun token(login: String, password: String): String



    class Base @Inject constructor(
        private val authorizationService: AuthorizationService,
        private val accountData: AccountDataStore,
        private val handleError: HandleError,
        private val db: MusicDBManager,
        private val imageLoader: ImageLoader
    ): AuthorizationRepository {

        override suspend fun logout(){
            accountData.saveData("","")
            db.clearAllTables()
            imageLoader.clearData()
        }

        override suspend fun token(login: String, password: String): String {
            return try {
                authorizationService.getToken(username = login, password = password).map(accountData)
                ""
            }catch (e: Exception){
                handleError.handle(e)
            }
        }

        override suspend fun isNotAuthorized(): Flow<Boolean> = accountData.isAccountDataEmpty()

    }
}