package com.example.musicapp.favorites.testcore

import com.example.musicapp.main.data.cache.AccountDataStore

/**
 * Created by HP on 15.03.2023.
 **/
class TokenStoreTest: AccountDataStore {
    var token = "token"

    override fun read(): String = token

    override fun save(data: String) {
        token = data
    }
}