package com.example.musicapp.main.data.cache

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Created by HP on 23.04.2023.
 **/

interface AccountDataStore {

    suspend fun saveData(token: String, ownerId: String)

    suspend fun token(): String

    suspend fun ownerId(): String

    suspend fun isAccountDataEmpty(): Flow<Boolean>

    class Base @Inject constructor(
        private val tokenStore: TokenStore,
        private val ownerIdStore: OwnerIdStore,
    ): AccountDataStore{

        override suspend fun saveData(token: String, ownerId: String) {
            tokenStore.save(token)
            ownerIdStore.save(ownerId)
        }

        override suspend fun token(): String = tokenStore.read().first()

        override suspend fun ownerId(): String = ownerIdStore.read().first()

        override suspend fun isAccountDataEmpty(): Flow<Boolean>
        = ownerIdStore.read().combine(tokenStore.read())
        {ownerId,token->
            return@combine ownerId.isEmpty() || token.isEmpty()
        }


    }
}