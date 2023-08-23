package com.kamancho.melisma.favorites.testcore

import com.kamancho.melisma.main.data.AuthorizationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by HP on 17.03.2023.
 **/
class TestAuthRepository: AuthorizationRepository{
    private var data = Pair("1","1")
    var tokenUpdate = false

    override suspend fun logout() {
        data = Pair("","")
    }

    override suspend fun token(login: String, password: String): String {
        tokenUpdate = true
        return ""
    }

    override suspend fun isNotAuthorized(): Flow<Boolean> {
        return flow { emit(data.first.isEmpty()||data.second.isEmpty()) }
    }

}