package com.example.musicapp.main.data


import com.example.musicapp.app.core.HandleError
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.MusicDBManager
import com.example.musicapp.app.vkdto.TokenDto
import com.example.musicapp.favorites.testcore.TestManagerResource
import com.example.musicapp.main.data.cache.AccountDataStore
import com.example.musicapp.main.data.cloud.AuthorizationService
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 29.04.2023.
 **/
class AuthorizationRepositoryTest {


    private lateinit var authorizationRepository: AuthorizationRepository
    private lateinit var service: TestAuthService
    private lateinit var tokenStore: TestTokenStore
    private lateinit var managerResource: ManagerResource
    private lateinit var manageDBManager: TestMusicDBManager

    @Before
    fun setup(){
        manageDBManager = TestMusicDBManager()
        service = TestAuthService()
        tokenStore = TestTokenStore()
        managerResource = TestManagerResource()
        authorizationRepository = AuthorizationRepository.Base(
            authorizationService = service,
            accountData = tokenStore,
            handleError = HandleError.Base(managerResource),
            db = manageDBManager
        )
    }


    @Test
    fun `test update token`() = runBlocking{
        service.token = TokenDto("2",2,2)

        authorizationRepository.token("","")

        assertEquals("2",tokenStore.token())
        assertEquals("2",tokenStore.ownerId())
    }

    @Test
    fun `test isNotAuthorized`() = runBlocking{
       tokenStore.saveData("2","2")

       val actual1 = authorizationRepository.isNotAuthorized().first()

        assertEquals(actual1,false)

        authorizationRepository.logout()
        val actual2 = authorizationRepository.isNotAuthorized().first()
        assertEquals(actual2,true)
    }

    @Test
    fun `test logout`() = runBlocking{

        authorizationRepository.logout()
        assertEquals(true,manageDBManager.isClear)
        assertEquals("",tokenStore.token())
        assertEquals("",tokenStore.ownerId())

    }

    class TestTokenStore: AccountDataStore{
        private var data = Pair("","")



        override suspend fun saveData(token: String, ownerId: String) {
            data = Pair(token,ownerId)
        }

        override suspend fun token(): String {
          return data.first
        }

        override suspend fun ownerId(): String {
           return data.second
        }

        override suspend fun isAccountDataEmpty(): Flow<Boolean> {
            return flow { emit(data.first.isEmpty()||data.second.isEmpty()) }
        }

    }

    class TestAuthService: AuthorizationService{
        var token = TokenDto("1",1,1)


        override suspend fun getToken(
            grantType: String,
            clientId: String,
            clientSecret: String,
            username: String,
            password: String,
            apiVersion: String,
            twoFaSupported: String,
        ): TokenDto {
           return token
        }

    }

    class TestMusicDBManager: MusicDBManager {
        var isClear = false

        override suspend fun clearAllTables() {
            isClear = true
        }
    }
}