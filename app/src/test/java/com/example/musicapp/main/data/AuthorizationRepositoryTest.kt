package com.example.musicapp.main.data

import TokenDto
import com.example.musicapp.main.data.cache.AccountDataStore
import com.example.musicapp.main.data.cloud.AuthorizationService
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 29.04.2023.
 **/
class AuthorizationRepositoryTest {


    lateinit var authorizationRepository: AuthorizationRepository
    lateinit var service: TestAuthService
    lateinit var tokenStore: TestTokenStore

    @Before
    fun setup(){
        service = TestAuthService()
        tokenStore = TestTokenStore()
        authorizationRepository = AuthorizationRepository.Base(
            authorizationService = service,
            accountData = tokenStore
        )
    }


    @Test
    fun `test update token`() = runBlocking{
        val expected = "token"
        service.token = expected
        authorizationRepository.updateToken()

        assertEquals(expected,tokenStore.read())
    }

    class TestTokenStore: AccountDataStore{
       private var token = ""

        override fun read(): String = token

        override fun save(data: String) {
            token = data
        }

    }

    class TestAuthService: AuthorizationService{
        var token = ""
        override suspend fun getToken(auth: String, content: String, grantType: String): TokenDto {
            return TokenDto(access_token = token, expires_in = 2, token_type = "dd")
        }

    }
}