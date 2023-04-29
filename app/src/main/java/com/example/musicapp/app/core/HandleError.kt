package com.example.musicapp.app.core

import com.example.musicapp.R
import com.example.musicapp.main.data.AuthorizationRepository
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Created by HP on 26.01.2023.
 **/
interface HandleError {

   suspend fun handle(e: Exception): String


    class Base @Inject constructor(
        private val managerResource: ManagerResource,
        private val auth: AuthorizationRepository
    ): HandleError {


        override suspend fun handle(e: Exception): String {
            var id = 0
            if(e is HttpException){
               id = when(e.code()){
                    202 -> R.string.accepted_message
                    204 -> R.string.no_content
                    400 -> R.string.bad_request
                    403 -> R.string.forbidenn
                    404 -> R.string.not_found
                    429 -> R.string.too_many_requests
                    500 -> R.string.server_error_message
                    502 -> R.string.bad_gatawey_message
                    else -> R.string.service_is_unavailable
                }
            }else if(e is UnknownHostException) id = R.string.no_connection_message
            else id =  R.string.service_is_unavailable

       return managerResource.getString(id)
        }



    }
}