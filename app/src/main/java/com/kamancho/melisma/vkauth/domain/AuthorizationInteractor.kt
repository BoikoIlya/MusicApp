package com.kamancho.melisma.vkauth.domain

import android.net.Uri
import android.util.Log
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.AuthRedirectException
import com.kamancho.melisma.app.core.HandleResponse
import com.kamancho.melisma.app.core.ManagerResource
import com.kamancho.melisma.main.data.AuthorizationRepository
import com.kamancho.melisma.vkauth.presentation.AuthViewModel
import javax.inject.Inject

/**
 * Created by HP on 15.09.2023.
 **/
interface AuthorizationInteractor {

    suspend fun token(
        login:String,
        password:String
    ): AuthResult

    suspend fun saveAccountData(successUrl:String)

    class Base @Inject constructor(
        private val authorizationRepository: AuthorizationRepository,
        private val handleResponse: HandleResponse,
        private val managerResource: ManagerResource,
    ): AuthorizationInteractor {

        override suspend fun token(login: String, password: String) = handleResponse.handle({
            return@handle if(login.isNotBlank() && password.isNotBlank()) {
                authorizationRepository.token(login.trim(),password.trim())
                AuthResult.Success
            }else AuthResult.Error(managerResource.getString(R.string.fill_all_fields))
        },{message,e->
           return@handle if(e is AuthRedirectException) AuthResult.Redirection(e.map(Unit))
            else AuthResult.Error(message)
        })

        override suspend fun saveAccountData(successUrl: String) {
            val uri = Uri.parse(successUrl)
            val fragment = uri.fragment
            val parameters = fragment?.split("&")?.associate {
                val (key, value) = it.split("=")
                key to value
            }

            val accessToken = parameters?.get(AuthViewModel.access_token)
            val userId = parameters?.get(AuthViewModel.user_id)
            authorizationRepository.saveAccountData(
                accessToken!!,
                userId!!
            )
        }


    }
}