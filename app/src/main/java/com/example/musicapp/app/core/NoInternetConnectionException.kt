package com.example.musicapp.app.core



abstract class AppException(
   message: String
): Exception(message){
   protected open fun map(handleError: HandleError): String = handleError.handle(this)
}
class NoInternetConnectionException(message: String): AppException(message)

class UnAuthorizedRequest(message: String): AppException(message){

    override fun map(handleError: HandleError): String {
        return ""
    }
}

