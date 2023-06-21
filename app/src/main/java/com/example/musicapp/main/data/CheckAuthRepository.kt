package com.example.musicapp.main.data

/**
 * Created by HP on 20.06.2023.
 **/
interface CheckAuthRepository {

    suspend fun isAuthorized(notAuthorized: ()->Unit)
}