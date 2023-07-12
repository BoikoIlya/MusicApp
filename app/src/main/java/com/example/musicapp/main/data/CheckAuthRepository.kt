package com.example.musicapp.main.data

import kotlinx.coroutines.flow.Flow

/**
 * Created by HP on 20.06.2023.
 **/
interface CheckAuthRepository {

    suspend fun isNotAuthorized(): Flow<Boolean>
}