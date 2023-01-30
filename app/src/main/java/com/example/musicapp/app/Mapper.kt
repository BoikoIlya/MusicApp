package com.example.musicapp.app

/**
 * Created by HP on 27.01.2023.
 **/
interface Mapper<T,R> {

    fun map(data: T): R
}