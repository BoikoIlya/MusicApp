package com.example.musicapp.trending.core

import com.example.musicapp.main.data.AuthorizationRepository

/**
 * Created by HP on 17.03.2023.
 **/
class AuthRepoTest: AuthorizationRepository {
    var token = false

    override suspend fun updateToken() {
        token = true
    }
}