package com.example.musicapp.favorites.data.cloud

import com.example.musicapp.app.core.UnAuthorizedException
import com.example.musicapp.app.core.UnAuthorizedResponseHandler

/**
 * Created by HP on 24.06.2023.
 **/
data class TrackIdResponse(private val response: Int?=null): UnAuthorizedResponseHandler<Int>{

    override fun handle(): Int = response?: throw UnAuthorizedException()

}
