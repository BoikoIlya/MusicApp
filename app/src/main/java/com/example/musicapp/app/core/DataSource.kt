package com.example.musicapp.app.core

import com.example.musicapp.R

/**
 * Created by HP on 08.07.2023.
 **/
interface DataSource<T,R> {

     fun handleResponse(data: T?):R

     abstract class Abstract<T,R>(
         private val mapper: Mapper<T,R>
     ): DataSource<T, R>{

         override fun handleResponse(data: T?) = if(data!=null) mapper.map(data) else throw UnAuthorizedException()

     }
}