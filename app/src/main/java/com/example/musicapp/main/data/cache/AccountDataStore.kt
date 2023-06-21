package com.example.musicapp.main.data.cache

import javax.inject.Inject

/**
 * Created by HP on 23.04.2023.
 **/

interface AccountDataStore {

    suspend fun saveData(token: String, ownerId: String)

    suspend fun token(): String

    suspend fun ownerId(): String

    suspend fun isDataEmpty(): Boolean

    class Base @Inject constructor(
        private val tokenStore: TokenStore,
        private val ownerIdStore: OwnerIdStore,
    ): AccountDataStore{

        override suspend fun saveData(token: String, ownerId: String) {
            tokenStore.save(token)
            ownerIdStore.save(ownerId)
        }

        override suspend fun token(): String = tokenStore.read()

        override suspend fun ownerId(): String = ownerIdStore.read()

        override suspend fun isDataEmpty(): Boolean
        = ownerIdStore.read().isEmpty() || tokenStore.read().isEmpty()

    }
}