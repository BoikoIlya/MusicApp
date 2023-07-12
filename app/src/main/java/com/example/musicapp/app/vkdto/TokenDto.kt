package com.example.musicapp.app.vkdto

import com.example.musicapp.main.data.cache.AccountDataStore

data class TokenDto(
   private val access_token: String,
   private val expires_in: Int,
   private val user_id: Int
){

    suspend fun map(accountDataStore: AccountDataStore){
        accountDataStore.saveData(access_token,user_id.toString())
    }

}