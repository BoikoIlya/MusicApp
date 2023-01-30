package com.example.musicapp.app

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

interface ManagerResource {

    fun getString(@StringRes id: Int): String

    class Base @Inject constructor(
        private val context: Context
    ): ManagerResource{
        override fun getString(id: Int) = context.getString(id)

    }

}
