package com.kamancho.melisma.app.core

/**
 * Created by HP on 30.01.2023.
 **/
interface ClickListener<T> {

    fun onClick(data: T)
}

interface Selector<T>{
    fun onSelect(data: T, position: Int)
}