package com.example.musicapp.app.core

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.palette.graphics.Palette
import com.example.musicapp.R

/**
 * Created by HP on 10.07.2023.
 **/
sealed interface PaintBackgroundShadow {

    fun paint(view: CardView, resource: Bitmap)

    object ApiPAndAbove : PaintBackgroundShadow {

        @RequiresApi(Build.VERSION_CODES.P)
        override fun paint(view: CardView, resource: Bitmap) {
            Palette.from(resource).generate(){ palette->
                view.outlineSpotShadowColor = palette?.vibrantSwatch?.rgb?: R.color.black
            }
        }
    }

    object BelowApiP: PaintBackgroundShadow {
        override fun paint(view: CardView, resource: Bitmap) = Unit
    }
}