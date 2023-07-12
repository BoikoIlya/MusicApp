package com.example.musicapp.app.vkdto

import com.example.musicapp.app.core.UnAuthorizedException
import com.example.musicapp.app.core.UnAuthorizedResponseHandler

data class Response(
   private val count: Int,
   private val items: List<Item>?
): UnAuthorizedResponseHandler<List<Item>>{

    override fun handle(): List<Item> = items ?: throw UnAuthorizedException()
}