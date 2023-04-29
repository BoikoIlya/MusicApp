package com.example.musicapp.app.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ColorSpace.Rgb
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.musicapp.R
import javax.inject.Inject
import kotlin.math.log

/**
 * Created by HP on 30.01.2023.
 **/
interface ImageLoader {

    fun loadImage(
        url:String,
        target: ImageView,
    )

    fun loadImage(
        url:String,
        target: ImageView,
        imgBg: CardView,
    )



    class Base @Inject constructor(): ImageLoader {

        override fun loadImage(url: String, target: ImageView) {
            Glide.with(target)
                .load(url)
                .placeholder(R.drawable.tone)
                .into(target)
        }

        override fun loadImage(url: String, target: ImageView, imgBg: CardView) {
            Glide.with(target)
                .asBitmap()
                .load(url)
                .placeholder(R.drawable.tone)
                .listener(object :RequestListener<Bitmap>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean,
                    ): Boolean = false

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        Palette.from(resource!!).generate(){palette->
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                imgBg.outlineSpotShadowColor = palette?.vibrantSwatch?.rgb?: R.color.black
                            }
                        }
                        return false
                    }


                })
                .into(target)
        }

    }
}