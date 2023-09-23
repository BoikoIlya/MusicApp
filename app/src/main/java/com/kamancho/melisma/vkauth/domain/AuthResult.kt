package com.kamancho.melisma.vkauth.domain

/**
 * Created by HP on 15.09.2023.
 **/

sealed interface AuthResult {

    suspend fun map(mapper: Mapper)

    interface Mapper{

        suspend fun map(result: AuthResult.Success)
        suspend fun map(result: AuthResult.Error,message:String)
        suspend fun map(result: AuthResult.Redirection, redirectionUrl: String)

    }


    object Success: AuthResult{
        override suspend fun map(mapper: Mapper) = mapper.map(this)
    }

    data class Error(
        private val message: String
    ): AuthResult {
        override suspend fun map(mapper: Mapper) = mapper.map(this,message)
    }

    data class Redirection(private val url: String): AuthResult {
        override suspend fun map(mapper: Mapper) = mapper.map(this,url)
    }
}