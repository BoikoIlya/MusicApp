package com.example.musicapp.favorites.testcore

import com.example.musicapp.main.data.cache.AccountDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by HP on 15.03.2023.
 **/
class AccountDataStoreTest: AccountDataStore {
    var token = "token"
    var ownerId = "owner"


    override suspend fun saveData(token: String, ownerId: String) {
        this.token = token
        this.ownerId = ownerId
    }

    override suspend fun token(): String = token

    override suspend fun ownerId(): String = ownerId

    override suspend fun isAccountDataEmpty(): Flow<Boolean> = flow { emit(token.isEmpty() || ownerId.isEmpty()) }
}