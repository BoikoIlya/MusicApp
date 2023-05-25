package com.example.musicapp.app.core

import kotlinx.coroutines.Job

/**
 * Created by HP on 23.05.2023.
 **/

interface Remover{

    fun removeItem(id: String): Job

}