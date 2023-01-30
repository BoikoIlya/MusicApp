package com.example.musicapp

/**
 * Created by HP on 30.01.2023.
 **/
interface ClickListener<T> {

    fun onClick(data: T)
}