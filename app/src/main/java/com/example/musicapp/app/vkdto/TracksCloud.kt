package com.example.musicapp.app.vkdto

import com.example.musicapp.app.core.UnAuthorizedException
import com.example.musicapp.app.core.UnAuthorizedResponseHandler

data class TracksCloud(
   private val response: Response?=null
): UnAuthorizedResponseHandler<List<Item>> {

    override fun handle(): List<Item> = response?.handle()?: throw UnAuthorizedException()

}