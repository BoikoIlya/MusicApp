package com.kamancho.melisma.main.data

import com.google.gson.JsonParser
import com.kamancho.melisma.app.core.AuthRedirectException
import javax.inject.Inject
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.app.core.MusicDBManager
import com.kamancho.melisma.main.data.cache.AccountDataStore
import com.kamancho.melisma.main.data.cloud.AuthorizationCloudDataSource
import kotlinx.coroutines.flow.Flow

/**
 * Created by HP on 09.03.2023.
 **/
interface AuthorizationRepository: CheckAuthRepository {

   suspend fun logout()

   suspend fun token(login: String, password: String)

   suspend fun saveAccountData(token: String, clientId: String)

    class Base @Inject constructor(
        private val cloud: AuthorizationCloudDataSource,
        private val accountDataStore: AccountDataStore,
        private val db: MusicDBManager,
        private val imageLoader: ImageLoader,
    ): AuthorizationRepository {

        override suspend fun logout(){
            accountDataStore.saveData("","")
            db.clearAllTables()
            imageLoader.clearData()
        }

        override suspend fun token(login: String, password: String) =
           cloud.token(login,password).map(accountDataStore)


        override suspend fun saveAccountData(token: String, clientId: String) {
            accountDataStore.saveData(token,clientId)
        }


        override suspend fun isNotAuthorized(): Flow<Boolean> = accountDataStore.isAccountDataEmpty()

    }
}