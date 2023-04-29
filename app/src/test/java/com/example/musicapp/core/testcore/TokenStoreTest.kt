package com.example.musicapp.core.testcore

import com.example.musicapp.main.data.cache.TokenStore

/**
 * Created by HP on 15.03.2023.
 **/
class TokenStoreTest: TokenStore {
    var token = "token"

    override fun read(): String = token

    override fun save(data: String) {
        token = data
    }
}