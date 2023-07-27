package com.example.musicapp.main.data

import com.example.musicapp.main.data.cloud.AuthorizationService
import javax.inject.Inject
import com.example.musicapp.app.core.HandleError
import com.example.musicapp.app.core.MusicDatabase
import com.example.musicapp.main.data.cache.AccountDataStore
import kotlinx.coroutines.flow.Flow

/**
 * Created by HP on 09.03.2023.
 **/
interface AuthorizationRepository: CheckAuthRepository {

   suspend fun clearData()

   suspend fun token(login: String, password: String): String



    class Base @Inject constructor(
        private val authorizationService: AuthorizationService,
        private val accountData: AccountDataStore,
        private val handleError: HandleError,
        private val db: MusicDatabase
    ): AuthorizationRepository {
        override suspend fun clearData(){
            accountData.saveData("","")
            db.clearAllTables()
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