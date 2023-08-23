package com.kamancho.melisma.app.core

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import javax.inject.Inject

interface ManagerResource {

    fun getString(@StringRes id: Int): String

    fun getColor(@ColorRes id: Int): Int

    fun getBitmap(@DrawableRes id: Int): Bitmap

    fun getDimensionPixelSize(@DimenRes id: Int): Int

    fun getPackageName(): String

    fun getColorStateList(@ColorRes id: Int):ColorStateList

    class Base @Inject constructor(
        private val context: Context
    ): ManagerResource {
        override fun getBitmap(id: Int): Bitmap = AppCompatResources.getDrawable(context,id)!!.toBitmap()
        override fun getDimensionPixelSize(id: Int): Int = context.resources.getDimensionPixelSize(id)
        override fun getPackageName(): String = context.packageName
        override fun getColorStateList(id: Int): ColorStateList = ContextCompat.getColorStateList(context,id)!!

        override fun getString(id: Int) = context.getString(id)
        override fun getColor(id: Int): Int = context.getColor(id)

    }

}
