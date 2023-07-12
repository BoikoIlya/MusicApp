package com.example.musicapp.app.core

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.example.musicapp.R
import javax.inject.Inject

interface ManagerResource {

    fun getString(@StringRes id: Int): String

    fun getColor(@ColorRes id: Int): Int

    fun getBitmap(@DrawableRes id: Int): Bitmap

    class Base @Inject constructor(
        private val context: Context
    ): ManagerResource {
        override fun getBitmap(id: Int): Bitmap = AppCompatResources.getDrawable(context,id)!!.toBitmap()
        override fun getString(id: Int) = context.getString(id)
        override fun getColor(id: Int): Int = context.getColor(id)

    }

}
