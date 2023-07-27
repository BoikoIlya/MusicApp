package com.example.musicapp.app.core

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.example.musicapp.R
import javax.inject.Inject


/**
 * Created by HP on 30.01.2023.
 **/
interface ImageLoader {

    fun loadImage(
        url:String,
        targetView: ImageView,
    )

    fun loadImage(
        url:String,
        targetView: ImageView,
        imgBg: CardView,
    )

    fun loadImageForPlaylist(
        url:String,
        targetView: ImageView,
    )


     class Base (
         private val paintBackgroundShadow: PaintBackgroundShadow,
     ): ImageLoader {


        override fun loadImage(url: String, targetView: ImageView) {

            Glide.with(targetView)
                .load(url)
                .placeholder(R.drawable.tone_yellow)
                .into(targetView)
        }

        override fun loadImage(
            url: String,
            targetView: ImageView,
            imgBg: CardView,
            ) {


            Glide.with(targetView)
                .asBitmap()
                .load(url)
                .transition(withCrossFade())
//                .thumbnail(
//                    Glide.with(targetView)
//                        .asBitmap()
//                        .load(url)
//                        .transition(withCrossFade())
//                        .override(68,68))
                .placeholder(R.drawable.tone_yellow)
                .listener(object :RequestListener<Bitmap>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        return true
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        paintBackgroundShadow.paint(imgBg,resource!!)
                        return false
                    }


                })
                .into(targetView)
        }

         override fun loadImageForPlaylist(url: String, targetView: ImageView) {
             Glide.with(targetView)
                 .load(url)
                 .placeholder(R.drawable.tone_yellow)
                 .override(300,300)
                 .into(targetView)
         }

     }
}