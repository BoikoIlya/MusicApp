package com.example.musicapp.favorites.data.cloud

import android.util.Log
import com.example.musicapp.app.core.UnAuthorizedException
import com.example.musicapp.app.core.UnAuthorizedResponseHandler
import com.example.musicapp.app.vkdto.ErrorResponse
import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


/**
 * Created by HP on 20.06.2023.
 **/

data class CountResponse(
    private val response: Int?=null
): UnAuthorizedResponseHandler<Int>{
    override fun handle(): Int = response?: throw UnAuthorizedException()

}

