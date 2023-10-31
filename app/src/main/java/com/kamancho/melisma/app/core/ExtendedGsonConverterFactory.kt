package com.kamancho.melisma.app.core


import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type

/**
 * Created by HP on 20.07.2023.
 **/
class ExtendedGsonConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {

    companion object {
        fun create(): ExtendedGsonConverterFactory {
            return create(Gson())
        }

        fun create(gson: Gson): ExtendedGsonConverterFactory {
            return ExtendedGsonConverterFactory(gson)
        }
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        return GsonResponseBodyConverter<Any>(gson, type)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> {
        return GsonRequestBodyConverter<Any>(gson, type)
    }

    internal class GsonRequestBodyConverter<T>(private val gson: Gson, private val type: Type) : Converter<T, RequestBody> {

        @Throws(IOException::class)
        override fun convert(value: T): RequestBody {
            return gson.toJson(value, type).toRequestBody()
        }
    }

    internal class GsonResponseBodyConverter<T>(private val gson: Gson, private val type: Type) : Converter<ResponseBody, T> {

        companion object{
             const val error_field = "error"
            private const val error_code_field = "error_code"
            private const val captcha_img_field = "captcha_img"
            private const val captcha_sid_field = "captcha_sid"
            private const val un_authorized_code = 5
            private const val captcha_code = 14
        }

        @Throws(IOException::class)
        override fun convert(value: ResponseBody): T {
                val jsonString = value.string()
                val json = JsonParser.parseString(jsonString).asJsonObject



                if (json.has(error_field))
                {
                    val error = json.getAsJsonObject(error_field)

                    val errorCode = error.get(error_code_field).toString().toInt()
                    Log.d("tag", "convert: $errorCode ")
                    throw when(errorCode){
                        un_authorized_code ->UnAuthorizedException()
                        captcha_code -> CaptchaNeededException(
                            error.get(captcha_sid_field).asString,
                            error.get(captcha_img_field).asString
                        )
                        else -> VkException.Base(errorCode)
                    }
                }
                else return gson.fromJson(jsonString, type)
        }
    }

}