package com.example.musicapp.favorites.testcore

import com.example.musicapp.main.data.AuthorizationRepository

/**
 * Created by HP on 17.03.2023.
 **/
class TestAuthRepo: AuthorizationRepository {
    var token = false

    override suspend fun updateToken() {
        token = true
    }
}